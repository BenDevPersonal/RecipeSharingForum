import { api } from "./api";

export const getPostById = async (id) => {
  const res = await api.get(`/api/posts/${id}`);
  return res.data;
};

export const getPosts = async () => {
  const res = await api.get(`/api/posts`);
  return res.data;
};

export const createPost = async (data, images) => {
  const formData = new FormData();

  // JSON part (post data)
  formData.append(
    "data",
    new Blob([JSON.stringify(data)], { type: "application/json" })
  );

  // images part
  if (images && images.length > 0) {
    images.forEach((img) => {
      formData.append("images", img);
    });
  }

  const res = await api.post("/api/posts", formData);
  return res.data;
};

export const updatePost = async (id, data, images) => {
  const formData = new FormData();

  formData.append(
    "data",
    new Blob([JSON.stringify(data)], { type: "application/json" })
  );

  if (images && images.length > 0) {
    images.forEach((img) => {
      formData.append("images", img);
    });
  }

  const res = await api.put(`/api/posts/update/${id}`, formData);
  return res.data;
};

export const deletePost = async (id) => {
  const res = await api.delete(`/api/posts/delete/${id}`);
  return res.data;
};