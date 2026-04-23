import { useState, useEffect, useRef } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { getMe, getUserById, updateProfile } from "../api/users";
import { getAllergies } from "../api/meta";
import {
    getUserSetting,
    createUserSetting,
    updateUserSetting,
} from "../api/settings";
import { ErrorMessage } from "../components/ErrorMessage";
import { computeReputation, getBadge } from "../utils/reputation";
import { getPosts } from "../api/posts";

export function Profile() {
    const queryClient = useQueryClient();
    const [editMode, setEditMode] = useState(false);
    const [form, setForm] = useState(null);

    const [tab, setTab] = useState("profile");
    const [settingsForm, setSettingsForm] = useState(null);

    const settingsInitializedRef = useRef(false);

    const { data: allPosts = [] } = useQuery({
        queryKey: ["allPosts"],
        queryFn: getPosts,
    });

    const {
        data: me,
        isLoading: meLoading,
        isError: meError,
        error: meErr,
    } = useQuery({
        queryKey: ["me"],
        queryFn: getMe,
    });

    const {
        data,
        isLoading,
        isError,
        error,
    } = useQuery({
        queryKey: ["profile", me?.id],
        enabled: !!me?.id,
        queryFn: () => getUserById(me.id),
    });

    // ✅ FIX: allow undefined state properly
    const {
        data: settings,
        isLoading: settingsLoading,
    } = useQuery({
        queryKey: ["settings", me?.id],
        enabled: !!me?.id,
        retry: false,
        queryFn: async () => {
            try {
                return await getUserSetting(me.id);
            } catch (err) {
                if (err?.response?.status === 404) return null;
                throw err;
            }
        },
    });

    const { data: allAllergies } = useQuery({
        queryKey: ["allergies"],
        queryFn: getAllergies,
    });

    const settingsMutation = useMutation({
        mutationFn: ({ id, payload }) => {
            if (id) return updateUserSetting(id, payload);
            return createUserSetting(payload);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["settings", me?.id] });
        },
    });

    // ✅ FIX: only run when NOT loading AND no settings exist
    useEffect(() => {
        if (!me?.id) return;
        if (settingsLoading) return;
        if (settings !== null && settings !== undefined) return;
        if (settingsInitializedRef.current) return;

        settingsInitializedRef.current = true;

        settingsMutation.mutate({
            id: null,
            payload: {
                userId: me.id,
                showCountryOnProfile: true,
                showAllergyOnProfile: true,
                autoFilterAllergy: false,
            },
        });
    }, [settings, me?.id, settingsLoading]);

    const mutation = useMutation({
        mutationFn: (updated) => updateProfile(me.id, updated),
        onSuccess: (updated) => {
            queryClient.setQueryData(["profile", me.id], updated);
            setEditMode(false);
        },
    });

    const { reputation } = computeReputation(allPosts, data?.id);
    const badge = getBadge(reputation);

    useEffect(() => {
        if (data) {
            setForm({
                login: data.login,
                password: "",
                country: data.country,
                allergies: data.allergies || [],
                role: data.role,
                email: data.email,
            });
        }
    }, [data]);

    // ✅ FIX: always set settingsForm even during loading states
    useEffect(() => {
        if (settings === undefined) return;

        setSettingsForm({
            showCountry: settings?.showCountryOnProfile ?? true,
            showAllergy: settings?.showAllergyOnProfile ?? true,
            autoFilter: settings?.autoFilterAllergy ?? false,
        });
    }, [settings]);

    if (meLoading || isLoading) {
        return <div className="p-10 text-gray-500">Loading profile...</div>;
    }

    if (meError) return <ErrorMessage message={meErr.message} />;
    if (isError) return <ErrorMessage message={error.message} />;

    if (!form) return null;

    function handleChange(e) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    function toggleAllergy(allergyName) {
        setForm((prev) => {
            const list = prev.allergies || [];
            const exists = list.includes(allergyName);

            return {
                ...prev,
                allergies: exists
                    ? list.filter((a) => a !== allergyName)
                    : [...list, allergyName],
            };
        });
    }

    function handleSave() {
        const allergyIds =
            allAllergies
                ?.filter((a) => form.allergies.includes(a.name))
                .map((a) => a.id) || [];

        mutation.mutate({
            login: form.login,
            password: form.password || null,
            country: form.country,
            role: form.role,
            allergyIds,
            email: form.email,
        });
    }

    function toggleSetting(name) {
        setSettingsForm((prev) => {
            const updated = { ...prev, [name]: !prev[name] };

            settingsMutation.mutate({
                id: settings?.id,
                payload: {
                    userId: me.id,
                    showCountryOnProfile: updated.showCountry,
                    showAllergyOnProfile: updated.showAllergy,
                    autoFilterAllergy: updated.autoFilter,
                },
            });

            return updated;
        });
    }

    return (
        <div className="max-w-2xl mx-auto px-6 py-10 space-y-6">

            <div className="flex gap-3">
                <button
                    onClick={() => setTab("profile")}
                    className={`px-4 py-2 rounded-xl ${tab === "profile" ? "bg-accent text-white" : "bg-gray-200 dark:bg-gray-800"}`}
                >
                    Profile
                </button>

                <button
                    onClick={() => setTab("settings")}
                    className={`px-4 py-2 rounded-xl ${tab === "settings" ? "bg-accent text-white" : "bg-gray-200 dark:bg-gray-800"}`}
                >
                    Settings
                </button>
            </div>

            {/* PROFILE TAB */}
            {tab === "profile" && (
                <>
                    <div className="flex justify-between items-center">
                        <h1 className="text-3xl font-bold">My Profile</h1>

                        {!editMode ? (
                            <button
                                onClick={() => setEditMode(true)}
                                className="px-4 py-2 bg-accent text-white rounded-xl"
                            >
                                Edit Profile
                            </button>
                        ) : (
                            <div className="flex gap-2">
                                <button
                                    onClick={() => setEditMode(false)}
                                    className="px-4 py-2 bg-gray-300 dark:bg-gray-700 rounded-xl"
                                >
                                    Cancel
                                </button>

                                <button
                                    onClick={handleSave}
                                    className="px-4 py-2 bg-accent text-white rounded-xl"
                                >
                                    Save
                                </button>
                            </div>
                        )}
                    </div>

                    <div className="bg-white dark:bg-gray-900 p-6 rounded-2xl shadow-soft space-y-4">
                        <Field label="Login" value={form.login} editMode={editMode} name="login" onChange={handleChange} />
                        <Field label="Email" value={form.email} editMode={false} />

                        <p className="text-sm text-gray-500">Badge</p>
                        <div className="text-left space-y-1">
                            <div className="inline-flex w-fit text-xs px-2 py-1 rounded-full text-green-700 dark:text-green-300 bg-green-100 dark:bg-green-900 border border-green-200 dark:border-green-300">
                                {badge}
                            </div>
                            <div className="text-xs text-gray-500">
                                Reputation: {reputation}
                            </div>
                        </div>

                        <Field label="Country" value={form.country} editMode={editMode} name="country" onChange={handleChange} />
                        <Field label="Role" value={form.role} editMode={false} />

                        <div>
                            <p className="text-sm text-gray-500">Allergies</p>

                            {editMode ? (
                                <div className="flex flex-wrap gap-2 mt-2">
                                    {allAllergies?.map((a) => (
                                        <button
                                            key={a.id}
                                            type="button"
                                            onClick={() => toggleAllergy(a.name)}
                                            className={`px-3 py-1 rounded-full border text-sm transition ${form.allergies.includes(a.name)
                                                ? "bg-red-500 text-white border-red-500"
                                                : "bg-gray-100 dark:bg-gray-800 border-gray-300 dark:border-gray-700"
                                                }`}
                                        >
                                            {a.name}
                                        </button>
                                    ))}
                                </div>
                            ) : (
                                <p className="font-medium">
                                    {form.allergies.length ? form.allergies.join(", ") : "-"}
                                </p>
                            )}
                        </div>
                    </div>
                </>
            )}

            {/* SETTINGS TAB */}
            {tab === "settings" && (
                <div className="bg-white dark:bg-gray-900 p-6 rounded-2xl space-y-4">
                    <SettingToggle label="Show country on profile" value={settingsForm?.showCountry} onClick={() => toggleSetting("showCountry")} />
                    <SettingToggle label="Show allergies on profile" value={settingsForm?.showAllergy} onClick={() => toggleSetting("showAllergy")} />
                    <SettingToggle label="Auto filter posts by allergies" value={settingsForm?.autoFilter} onClick={() => toggleSetting("autoFilter")} />
                </div>
            )}
        </div>
    );
}

/* helpers unchanged */
function Field({ label, value, editMode, name, onChange, type = "text" }) {
    return (
        <div>
            <p className="text-sm text-gray-500">{label}</p>
            {editMode && name ? (
                <input
                    name={name}
                    value={value}
                    onChange={onChange}
                    type={type}
                    className="w-full px-3 py-2 rounded-xl bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700"
                />
            ) : (
                <p className="font-medium">{value || "-"}</p>
            )}
        </div>
    );
}

function SettingToggle({ label, value, onClick }) {
    return (
        <div className="flex justify-between items-center">
            <span>{label}</span>

            <button
                onClick={onClick}
                className={`relative inline-flex items-center w-14 h-7 rounded-full transition-colors duration-300 ${value ? "bg-accent" : "bg-gray-300"}`}
            >
                <span
                    className={`absolute left-1 top-1 w-5 h-5 rounded-full bg-white shadow-md transform transition-transform duration-300 ${value ? "translate-x-7" : ""}`}
                />
            </button>
        </div>
    );
}