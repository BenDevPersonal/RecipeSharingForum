import { api } from "../api";

export const getUsers = async () => {
  const res = await api.get("/api/admin/users");
  return res.data;
};

export const deleteUser = async (id) => {
  await api.delete(`/api/admin/users/${id}`);
};