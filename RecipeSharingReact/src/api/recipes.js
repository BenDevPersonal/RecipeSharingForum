import { api } from "./api";

export const getRecipes = async (categoryId) => {
  const params = {};

  if (categoryId !== null && categoryId !== undefined) {
    params.categoryId = categoryId;
  }

  const res = await api.get("/api/posts", { params });
  return res.data;
};