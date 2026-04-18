import { useState } from "react";
import { login as apiLogin } from "../api/auth";
import { useNavigate } from "react-router-dom";
import { ErrorMessage } from "../components/ErrorMessage";
import { useAuth } from "../context/useAuth";

export function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const inputClass =
    "w-full p-3 rounded-lg border bg-white dark:bg-gray-900 text-gray-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-accent";

  const [form, setForm] = useState({
    login: "",
    password: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e) =>
    setForm((p) => ({ ...p, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const token = await apiLogin(form);
      login(token);

      navigate("/");
    } catch (err) {
      setError(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Login failed"
      );
    }
  };

  return (
    <div className="max-w-md mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6 text-center">🔐 Login</h1>

      {error && <ErrorMessage message={error} />}

      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          name="login"
          placeholder="Login"
          value={form.login}
          onChange={handleChange}
          className={inputClass}
          required
        />

        <input
          name="password"
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          className={inputClass}
          required
        />

        <button
          type="submit"
          className="w-full bg-accent text-white py-3 rounded-lg"
        >
          Login
        </button>
      </form>
    </div>
  );
}