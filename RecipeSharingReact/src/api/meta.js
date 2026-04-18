import { api } from "./api";

export const getCountries = async () => {
  const res = await api.get("/api/countries");
  return res.data;
};

export const getAllergies = async () => {
  const res = await api.get("/api/allergies");
  return res.data;
};