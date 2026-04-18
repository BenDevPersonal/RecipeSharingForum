import { api } from "../api";

export const getAllPosts = async () => {
  const res = await api.get("/api/admin/posts");
  return res.data;
};

export const deletePost = async (id) => {
  await api.delete(`/api/admin/posts/${id}`);
};