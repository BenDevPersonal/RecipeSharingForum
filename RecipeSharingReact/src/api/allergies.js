import { api } from "./api";

export const getAllergies = async () => {
  const res = await api.get("/api/allergies");
  return res.data;
};