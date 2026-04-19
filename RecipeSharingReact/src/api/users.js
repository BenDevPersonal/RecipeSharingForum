import { api } from "./api";

export const getUsers = async () => {
    const res = await api.get(`/api/users`);
    return res.data;
}

export const getUserById = async (id) => {
  const res = await api.get(`/api/users/${id}`);
  return res.data;
};

export const getMe = async () => {
  const res = await api.get("/api/users/me");
  return res.data;
};

export const updateProfile = async (id, data) => {
  const res = await api.put(`/api/users/update/${id}`, data);
  return res.data;
};

export const deleteUser = async (id) => {
  const res = await api.delete(`/api/users/delete/${id}`);
  return res.data;
};