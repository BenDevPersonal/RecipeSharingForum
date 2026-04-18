import { api } from "./api";

export const register = async (data) => {
  try {
    const res = await api.post("/api/auth/register", data);
    return res.data;
  } catch (err) {
    const message =
      err?.response?.data?.message ||
      err?.response?.data ||
      "Registration failed";

    throw new Error(message);
  }
};

export const login = async (data) => {
  try {
    const res = await api.post("/api/auth/login", data);
    return res.data;
  } catch (err) {
    const message =
      err?.response?.data?.message ||
      err?.response?.data ||
      "Login failed";

    throw new Error(message);
  }
};