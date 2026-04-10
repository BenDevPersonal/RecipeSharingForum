import { api } from "./api";

export const getRecipes = async (categoryId) => {
  const res = await api.get("/api/posts", {
    params: { categoryId },
  });
  return res.data;
};