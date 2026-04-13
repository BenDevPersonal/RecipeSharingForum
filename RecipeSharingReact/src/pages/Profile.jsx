import { useState, useEffect } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { getUserById, updateProfile } from "../api/users";
import { ErrorMessage } from "../components/ErrorMessage";

const MOCK_USER_ID = 1;

const mockAllergies = [
  "Gluten",
  "Lactose",
  "Nuts",
  "Eggs",
];

export function Profile() {
  const queryClient = useQueryClient();
  const [editMode, setEditMode] = useState(false);
  const [form, setForm] = useState(null);

  const { data, isLoading, isError, error } = useQuery({
    queryKey: ["me"],
    queryFn: () => getUserById(MOCK_USER_ID),
  });

  const mutation = useMutation({
    mutationFn: (updated) => updateProfile(MOCK_USER_ID, updated),
    onSuccess: (updated) => {
      queryClient.setQueryData(["me"], updated);
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

  if (isLoading) return <div className="p-10 text-gray-500">Loading profile...</div>;
  if (isError) return <ErrorMessage message={error.message} />;
  if (!form) return null;

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function toggleAllergy(allergy) {
    setForm((prev) => {
      const exists = prev.allergies.includes(allergy);

      return {
        ...prev,
        allergies: exists
          ? prev.allergies.filter((a) => a !== allergy)
          : [...prev.allergies, allergy],
      };
    });
  }

  function handleSave() {
    mutation.mutate({
      login: form.login,
      password: form.password,
      country: form.country,
      allergies: form.allergies,
      email: form.email,
      role: form.role,
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

        <Field label="Login" value={form.login} editMode={editMode} name="login" onChange={handleChange} />

        <Field label="Email" value={form.email} editMode={false} />

        <Field label="Country" value={form.country} editMode={editMode} name="country" onChange={handleChange} />

        <Field label="Role" value={form.role} editMode={false} />

        {/* ✅ NEW ALLERGIES UI */}
        <div>
          <p className="text-sm text-gray-500">Allergies</p>

          {editMode ? (
            <div className="flex flex-wrap gap-2 mt-2">
              {mockAllergies.map((a) => (
                <button
                  key={a}
                  onClick={() => toggleAllergy(a)}
                  className={`px-3 py-1 rounded-full border text-sm transition ${
                    form.allergies.includes(a)
                      ? "bg-red-500 text-white border-red-500"
                      : "bg-gray-100 dark:bg-gray-800 border-gray-300 dark:border-gray-700"
                  }`}
                >
                  {a}
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