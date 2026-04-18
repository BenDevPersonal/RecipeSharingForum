import { api } from "./api";

export const getPostById = async (id) => {
  const res = await api.get(`/api/posts/${id}`);
  return res.data;
};

export const getPosts = async () => {
  const res = await api.get(`/api/posts`);
  return res.data;
};

export const createPost = async (data) => {
  const res = await api.post(`/api/posts`, data);
  return res.data;
};

export const updatePost = async (id, data) => {
  const res = await api.put(`/api/posts/update/${id}`, data);
  return res.data;
};

export const deletePost = async (id) => {
  const res = await api.delete(`/api/posts/delete/${id}`);
  return res.data;
};