import { api } from "./api";

export const createFeedback = async (data) => {
  const res = await api.post("/api/feedbacks", data);
  return res.data;
};

export const updateFeedback = async ({ id, data }) => {
  const res = await api.put(`/api/feedbacks/update/${id}`, data);
  return res.data;
};

export const deleteFeedback = async (id) => {
  const res = await api.delete(`/api/feedbacks/delete/${id}`);
  return res.data;
};