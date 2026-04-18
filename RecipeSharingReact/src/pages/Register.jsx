import { useEffect, useState } from "react";
import { register as registerApi, login as loginApi } from "../api/auth";
import { getAllergies, getCountries } from "../api/meta";
import { ErrorMessage } from "../components/ErrorMessage";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";

export function Register() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const inputClass =
    "w-full p-3 rounded-lg border bg-white dark:bg-gray-900 text-gray-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-accent";

  const [form, setForm] = useState({
    login: "",
    email: "",
    password: "",
    countryCode: "",
    allergyIds: [],
  });

  const [error, setError] = useState("");
  const [countries, setCountries] = useState([]);
  const [allergies, setAllergies] = useState([]);
  const [allergyQuery, setAllergyQuery] = useState("");

  useEffect(() => {
    const load = async () => {
      try {
        const [c, a] = await Promise.all([getCountries(), getAllergies()]);
        setCountries(c);
        setAllergies(a);
      } catch {
        setError("Failed to load form data");
      }
    };

    load();
  }, []);

  const handleChange = (e) =>
    setForm((p) => ({ ...p, [e.target.name]: e.target.value }));

  const addAllergy = (id) =>
    setForm((p) => ({
      ...p,
      allergyIds: p.allergyIds.includes(id)
        ? p.allergyIds
        : [...p.allergyIds, id],
    }));

  const removeAllergy = (id) =>
    setForm((p) => ({
      ...p,
      allergyIds: p.allergyIds.filter((x) => x !== id),
    }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      // 1. register user
      await registerApi(form);

      // 2. auto-login
      const token = await loginApi({
        login: form.login,
        password: form.password,
      });

      // 3. IMPORTANT: sync auth context (NO localStorage manual write)
      login(token);

      // 4. redirect
      navigate("/");
    } catch (err) {
      setError(
        err.response?.data?.message ||
          err.response?.data ||
          err.message ||
          "Registration failed"
      );
    }
  };

  const filteredAllergies = allergies.filter((a) =>
    a.name.toLowerCase().includes(allergyQuery.toLowerCase())
  );

  return (
    <div className="max-w-md mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6 text-center">📝 Register</h1>

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
          name="email"
          type="email"
          placeholder="Email"
          value={form.email}
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

        <select
          name="countryCode"
          value={form.countryCode}
          onChange={handleChange}
          className={inputClass}
          required
        >
          <option value="">Select country</option>
          {countries.map((c) => (
            <option key={c.code} value={c.code}>
              {c.name}
            </option>
          ))}
        </select>

        <div>
          <p className="text-sm mb-2">Allergies (optional)</p>

          <div className="flex flex-wrap gap-2 mb-2">
            {allergies
              .filter((a) => form.allergyIds.includes(a.id))
              .map((a) => (
                <span
                  key={a.id}
                  onClick={() => removeAllergy(a.id)}
                  className="bg-accent text-white px-2 py-1 rounded-full text-sm cursor-pointer"
                >
                  {a.name} ✕
                </span>
              ))}
          </div>

          <input
            placeholder="Search allergies..."
            value={allergyQuery}
            onChange={(e) => setAllergyQuery(e.target.value)}
            className={inputClass}
          />

          <div className="border rounded-lg max-h-40 overflow-y-auto mt-2">
            {filteredAllergies
              .filter((a) => !form.allergyIds.includes(a.id))
              .map((a) => (
                <div
                  key={a.id}
                  onClick={() => addAllergy(a.id)}
                  className="p-2 cursor-pointer hover:bg-gray-100 dark:hover:bg-gray-800"
                >
                  {a.name}
                </div>
              ))}
          </div>
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