import { api } from "../api";

export const getAllergies = async () => {
  const res = await api.get("/api/allergies");
  return res.data;
};

export const createAllergy = (data) =>
  api.post("/api/allergies", data);

export const updateAllergy = (id, data) =>
  api.put(`/api/allergies/${id}`, data);

export const deleteAllergy = (id) =>
  api.delete(`/api/allergies/${id}`);