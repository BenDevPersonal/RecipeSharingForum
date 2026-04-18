import { useState, useEffect } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { getMe, getUserById, updateProfile } from "../api/users";
import { getAllergies } from "../api/meta";
import { ErrorMessage } from "../components/ErrorMessage";

export function Profile() {
  const queryClient = useQueryClient();
  const [editMode, setEditMode] = useState(false);
  const [form, setForm] = useState(null);

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

  const { data: allAllergies } = useQuery({
    queryKey: ["allergies"],
    queryFn: getAllergies,
  });

  const mutation = useMutation({
    mutationFn: (updated) => updateProfile(me.id, updated),
    onSuccess: (updated) => {
      queryClient.setQueryData(["profile", me.id], updated);
      setEditMode(false);
    },
  });

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

  if (meLoading || isLoading)
    return <div className="p-10 text-gray-500">Loading profile...</div>;

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

  return (
    <div className="max-w-2xl mx-auto px-6 py-10 space-y-6">

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

        <Field
          label="Login"
          value={form.login}
          editMode={editMode}
          name="login"
          onChange={handleChange}
        />

        <Field label="Email" value={form.email} editMode={false} />

        <Field
          label="Country"
          value={form.country}
          editMode={editMode}
          name="country"
          onChange={handleChange}
        />

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
                  className={`px-3 py-1 rounded-full border text-sm transition ${
                    form.allergies.includes(a.name)
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

        {editMode && (
          <Field
            label="Password"
            value={form.password}
            editMode={true}
            name="password"
            type="password"
            onChange={handleChange}
          />
        )}
      </div>
    </div>
  );
}

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