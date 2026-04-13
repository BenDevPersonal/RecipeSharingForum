import { useState } from "react";
import { register } from "../api/auth";
import { ErrorMessage } from "../components/ErrorMessage";

export function Register() {
  const [form, setForm] = useState({
    login: "",
    email: "",
    password: "",
    countryId: "",
    allergyIds: [],
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleAllergyChange = (e) => {
    const value = parseInt(e.target.value);

    setForm((prev) => ({
      ...prev,
      allergyIds: prev.allergyIds.includes(value)
        ? prev.allergyIds.filter((id) => id !== value)
        : [...prev.allergyIds, value],
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await register(form);
      alert("Registered successfully!");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="max-w-md mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6 text-center">
        📝 Register
      </h1>

      {error && <ErrorMessage message={error} />}

      <form onSubmit={handleSubmit} className="space-y-4">

        <input
          name="login"
          placeholder="Login"
          value={form.login}
          onChange={handleChange}
          className="w-full p-3 rounded-lg border text-gray-900 dark:text-white bg-white dark:bg-gray-900"
          required
        />

        <input
          name="email"
          type="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
          className="w-full p-3 rounded-lg border text-gray-900 dark:text-white bg-white dark:bg-gray-900"
          required
        />

        <input
          name="password"
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          className="w-full p-3 rounded-lg border text-gray-900 dark:text-white bg-white dark:bg-gray-900"
          required
        />

        <input
          name="country"

          placeholder="Country "
          value={form.country}
          onChange={handleChange}
          className="w-full p-3 rounded-lg border text-gray-900 dark:text-white bg-white dark:bg-gray-900"
          required
        />

        {/* Optional allergies */}
        <div>
          <p className="text-sm mb-2">Allergies (optional)</p>

          <label className="block">
            <input type="checkbox" value={1} onChange={handleAllergyChange} />
            Peanut
          </label>

          <label className="block">
            <input type="checkbox" value={2} onChange={handleAllergyChange} />
            Gluten
          </label>

          <label className="block">
            <input type="checkbox" value={3} onChange={handleAllergyChange} />
            Milk
          </label>
        </div>

        <button
          type="submit"
          className="w-full bg-accent text-white py-3 rounded-lg"
        >
          Register
        </button>
      </form>
    </div>
  );
}