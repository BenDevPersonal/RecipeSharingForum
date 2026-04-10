import { useQuery } from "@tanstack/react-query";
import { getProfile } from "../api/user";
import { ErrorMessage } from "../components/ErrorMessage";

export function Profile() {
  const {
    data: user,
    isLoading,
    isError,
    error,
  } = useQuery({
    queryKey: ["profile"],
    queryFn: getProfile,
  });

  if (isLoading) {
    return (
      <div className="p-10 text-center text-gray-500">
        Loading profile...
      </div>
    );
  }

  if (isError) {
  return (
    <div className="p-10">
      <ErrorMessage message={error.message} className="text-center" />
    </div>
  );
}

  return (
    <div className="max-w-3xl mx-auto px-6 py-10">
      
      <h1 className="text-4xl font-bold mb-8 text-center">
        👤 Profile
      </h1>

      <div className="bg-white dark:bg-gray-900 rounded-2xl shadow-soft p-8 space-y-6">

        <ProfileField label="Login" value={user.login} />
        <ProfileField label="Email" value={user.email} />
        <ProfileField label="Country" value={user.country?.name} />

        <ProfileField
          label="Password"
          value="••••••••"
        />

        <div>
          <p className="text-sm text-gray-500 dark:text-gray-400 mb-1">
            Role
          </p>
          <span
            className={`px-3 py-1 rounded-full text-xs font-semibold ${
              user.role?.name === "admin"
                ? "bg-red-100 text-red-600 dark:bg-red-900/30 dark:text-red-400"
                : "bg-green-100 text-green-600 dark:bg-green-900/30 dark:text-green-400"
            }`}
          >
            {user.role?.name}
          </span>
        </div>

      </div>
    </div>
  );
}

function ProfileField({ label, value }) {
  return (
    <div>
      <p className="text-sm text-gray-500 dark:text-gray-400 mb-1">
        {label}
      </p>
      <p className="text-lg font-medium text-gray-800 dark:text-gray-200">
        {value || "-"}
      </p>
    </div>
  );
}