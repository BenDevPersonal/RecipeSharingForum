import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/auth";
import { AppContext } from "../App";
import { ErrorMessage } from "../components/ErrorMessage";

export function Login() {
  const [form, setForm] = useState({
    identifier: "",
    password: "",
  });

  const [error, setError] = useState("");

  const { setUser } = useContext(AppContext);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const data = await login(form);

      localStorage.setItem("token", data.token);

      setUser(data.user.login); // matches Home.jsx

      navigate("/");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="max-w-md mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6 text-center">
        🔐 Login
      </h1>

      {error && <ErrorMessage message={error} />}

      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          name="identifier"
          placeholder="Username or Email"
          value={form.identifier}
          onChange={handleChange}
          className="w-full p-3 rounded-lg border bg-white dark:bg-gray-900"
          required
        />

        <input
          name="password"
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          className="w-full p-3 rounded-lg border bg-white dark:bg-gray-900"
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