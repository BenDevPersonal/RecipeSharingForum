import { useSearchParams, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { searchPosts, searchUsers } from "../api/search";
import { ErrorMessage } from "../components/ErrorMessage";
import { useMemo } from "react";

export function Search() {
  const [params] = useSearchParams();
  const navigate = useNavigate();

  const q = params.get("q") || "";
  const mode = params.get("mode") || "all";
  const category = params.get("category") || "";
  const allergy = params.get("allergy") || "";

  const { data, isLoading, isError, error } = useQuery({
    queryKey: ["search", q, mode, category, allergy],
    queryFn: () => runSearch(q, mode, category, allergy),
    enabled: !!q,
  });

  function runSearch(q, mode, category, allergy) {
    if (mode === "posts") {
      return searchPosts(q, category, allergy).then((posts) => ({
        posts,
        users: [],
      }));
    }

    if (mode === "users") {
      return searchUsers(q).then((users) => ({
        posts: [],
        users,
      }));
    }

    return Promise.all([
      searchPosts(q, category, allergy),
      searchUsers(q),
    ]).then(([posts, users]) => ({
      posts,
      users,
    }));
  }

  if (!q) {
    return <div className="p-10 text-gray-500">No search query</div>;
  }

  if (isLoading) {
    return <div className="p-10 text-gray-500">Searching...</div>;
  }

  if (isError) {
    return <ErrorMessage message={error.message} />;
  }

  return (
    <div className="max-w-4xl mx-auto px-6 py-10 space-y-10">
      <h1 className="text-2xl font-bold">
        Results for "{q}"
      </h1>

      <SearchResults data={data} query={q} navigate={navigate} />
    </div>
  );
}

function SearchResults({ data, query, navigate }) {
  const sortedPosts = useMemo(() => {
    return [...(data.posts || [])].sort((a, b) => b.rating - a.rating);
  }, [data.posts]);

  return (
    <>
      <ResultSection
        title="Posts"
        items={sortedPosts}
        type="post"
        query={query}
        navigate={navigate}
      />

      <ResultSection
        title="Users"
        items={data.users}
        type="user"
        query={query}
        navigate={navigate}
      />
    </>
  );
}

function ResultSection({ title, items, type, query, navigate }) {
  if (!items || items.length === 0) return null;

  return (
    <div className="space-y-4">
      <h2 className="text-xl font-semibold">{title}</h2>

      <div className="space-y-3">
        {items.map((item) => (
          <ResultCard
            key={item.id}
            item={item}
            type={type}
            query={query}
            navigate={navigate}
          />
        ))}
      </div>
    </div>
  );
}

function ResultCard({ item, type, query, navigate }) {
  function highlight(text) {
    if (!query) return text;

    const parts = String(text).split(new RegExp(`(${query})`, "gi"));

    return parts.map((part, i) =>
      part.toLowerCase() === query.toLowerCase() ? (
        <span key={i} className="text-accent font-semibold">
          {part}
        </span>
      ) : (
        part
      )
    );
  }

  if (type === "post") {
    return (
      <div
        onClick={() => navigate(`/post/${item.id}`)}
        className="p-4 rounded-2xl bg-white dark:bg-gray-900 shadow-soft hover:shadow-md transition cursor-pointer"
      >
        <div className="font-semibold">{highlight(item.title)}</div>

        <div className="text-sm text-gray-500 mt-1">
          {item.author}
        </div>

        <div className="text-yellow-500 text-sm mt-2 flex items-center gap-2">
          {renderStars(item.rating)}
          <span className="text-gray-500">
            {item.rating?.toFixed?.(1) ?? item.rating}
          </span>
        </div>
      </div>
    );
  }

  return (
    <div
      onClick={() => navigate(`/user/${item.id}`)}
      className="p-4 rounded-2xl bg-white dark:bg-gray-900 shadow-soft hover:shadow-md transition cursor-pointer"
    >
      <div className="font-semibold">{highlight(item.login)}</div>
    </div>
  );
}

function renderStars(rating = 0) {
  const full = Math.round(rating);
  const empty = 5 - full;

  return (
    <>
      {"★".repeat(full)}
      {"☆".repeat(empty)}
    </>
  );
}