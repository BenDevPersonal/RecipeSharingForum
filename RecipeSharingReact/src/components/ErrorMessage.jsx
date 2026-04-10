export function ErrorMessage({ message, className = "" }) {
  return (
    <div
      className={`mb-6 p-4 rounded-lg bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400 ${className}`}
    >
      {message}
    </div>
  );
}