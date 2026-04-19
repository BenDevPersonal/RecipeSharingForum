import { api } from "./api";

export const getCategories = async () => {
  const res = await api.get("/api/categories");
  return res.data;
};

export const createCategory = async (data) => {
  const res = await api.post("/api/categories", data);
  return res.data;
};

export const updateCategory = async (id, data) => {
  const res = await api.put(`/api/categories/update/${id}`, data);
  return res.data;
};

export const deleteCategory = async (id) => {
  const res = await api.delete(`/api/categories/delete/${id}`);
  return res.data;
};