import { api } from "./api";

/*
export const getPost = async (id) => {
  const res = await api.get(`/api/posts/${id}`);
  return res.data;
};
*/

/* Temporary until backend security and auth is set up */
export const getPost = async (id) => {
  return {
    id: 1,
    author: "chefanna",
    title: "Classic Margherita Pizza",
    content:
      "A simple Italian pizza with tomatoes, mozzarella, and basil.",
    creationDate: "2026-01-10",
    updateDate: "2026-01-12",
    allergies: ["Gluten", "Dairy"],
    categories: ["Italian", "Pizza"],
    rating: 4.5,
    feedbacks: [
      {
        id: 1,
        author: "foodlover99",
        rating: 5,
        content: "Absolutely amazing recipe!",
      },
      {
        id: 2,
        author: "john_doe",
        rating: 4,
        content: "Very good, but I added extra cheese.",
      },
    ],
  };
};