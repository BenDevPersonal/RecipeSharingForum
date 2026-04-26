import { api } from "./api";

export const getUserSetting = async (id) => {
  const res = await api.get(`/api/settings/user/${id}`);
  return res.data;
};

export const createUserSetting = async (data) => {
  const res = await api.post("/api/settings", data);
  return res.data;
};

export const updateUserSetting = async (id, data) => {
  const res = await api.put(`/api/settings/update/${id}`, data);
  return res.data;
};