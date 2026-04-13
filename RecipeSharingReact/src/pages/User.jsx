import { useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { getUserById } from "../api/users";
import { ErrorMessage } from "../components/ErrorMessage";

export function User() {
  const { id } = useParams();

  const { data, isLoading, isError, error } = useQuery({
    queryKey: ["user", id],
    queryFn: () => getUserById(id),
  });

  if (isLoading) return <div className="p-10 text-gray-500">Loading user...</div>;
  if (isError) return <ErrorMessage message={error.message} />;
  if (!data) return <div className="p-10 text-gray-500">User not found</div>;

  return (
    <div className="max-w-2xl mx-auto px-6 py-10 space-y-6">

      <h1 className="text-3xl font-bold">{data.login}</h1>

      <div className="bg-white dark:bg-gray-900 p-6 rounded-2xl shadow-soft space-y-3">
        <p><span className="font-semibold">Email:</span> {data.email}</p>
        <p><span className="font-semibold">Country:</span> {data.country}</p>
        <p><span className="font-semibold">Role:</span> {data.role}</p>

        <div>
          <p className="font-semibold">Allergies:</p>
          {data.allergies.length ? (
            <div className="flex gap-2 flex-wrap mt-2">
              {data.allergies.map((a, i) => (
                <span key={i} className="px-3 py-1 bg-gray-200 dark:bg-gray-800 rounded-full text-sm">
                  {a}
                </span>
              ))}
            </div>
          ) : (
            <p className="text-gray-500">None</p>
          )}
        </div>
      </div>

    </div>
  );
}