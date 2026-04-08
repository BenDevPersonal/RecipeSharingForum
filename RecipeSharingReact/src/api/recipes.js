import { api } from "./api";

export const getRecipes = async (categoryId) => {
  const res = await api.get("/recipes", {
    params: { categoryId },
  });
  return res.data;
};