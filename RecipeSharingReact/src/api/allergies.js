import { api } from "./api";

export const getAllergies = async () => {
  const res = await api.get("/api/allergies");
  return res.data;
};

export const createAllergy = async (data) => {
  const res = await api.post("/api/allergies", data);
  return res.data;
};

export const updateAllergy = async (id, data) => {
  const res = await api.put(`/api/allergies/update/${id}`, data);
  return res.data;
};

export const deleteAllergy = async (id) => {
  const res = await api.delete(`/api/allergies/delete/${id}`);
  return res.data;
};