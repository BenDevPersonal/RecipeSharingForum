import { useSearchParams, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { searchPosts, searchUsers } from "../api/search";
import { getPosts } from "../api/posts";
import { getBlacklists } from "../api/blacklist";
import { ErrorMessage } from "../components/ErrorMessage";
import { SkeletonSection } from "../components/Skeleton";
import { highlightParts } from "../utils/highlight";
import { getBadge } from "../utils/reputation";
import { useMemo } from "react";

export function Search() {
    const [params] = useSearchParams();
    const navigate = useNavigate();

    const { data: allPosts = [] } = useQuery({
        queryKey: ["allPosts"],
        queryFn: () => getPosts(),
    });

    // ✅ FETCH BLACKLIST
    const { data: blacklistData = [] } = useQuery({
        queryKey: ["blacklist-all"],
        queryFn: () => getBlacklists(),
    });

    // ✅ EXTRACT IDS
    const blacklistedIds = useMemo(
        () => blacklistData.map((b) => b.blacklistedUserId),
        [blacklistData]
    );

    const q = params.get("q") || "";
    const mode = params.get("mode") || "all";
    const category = params.get("category") || "";
    const allergy = params.get("allergy") || "";

    const {
        data = { posts: [], users: [] },
        isLoading,
        isError,
        error,
    } = useQuery({
        queryKey: ["search", q, mode, category, allergy],
        queryFn: () => runSearch(q, mode, category, allergy),
        enabled: !!q,
    });

    function runSearch(q, mode, category, allergy) {
        if (mode === "posts") {
            return searchPosts(q, category, allergy).then((posts) => ({
                posts: posts || [],
                users: [],
            }));
        }

        if (mode === "users") {
            return searchUsers(q).then((users) => ({
                posts: [],
                users: users || [],
            }));
        }

        return Promise.all([
            searchPosts(q, category, allergy),
            searchUsers(q),
        ]).then(([posts, users]) => ({
            posts: posts || [],
            users: users || [],
        }));
    }

    if (!q) {
        return <div className="p-10 text-gray-500">No search query</div>;
    }

    if (isLoading) {
        return (
            <div className="max-w-4xl mx-auto px-6 py-10 space-y-10">
                <h1 className="text-2xl font-bold">Searching "{q}"...</h1>

                <div className="space-y-6">
                    <SkeletonSection title="Posts" />
                    <SkeletonSection title="Users" />
                </div>
            </div>
        );
    }

    if (isError) {
        return <ErrorMessage message={error.message} />;
    }

    return (
        <div className="max-w-4xl mx-auto px-6 py-10 space-y-10">
            <h1 className="text-2xl font-bold">Results for "{q}"</h1>

            <SearchResults
                data={data}
                query={q}
                navigate={navigate}
                params={params}
                allPosts={allPosts}
                blacklistedIds={blacklistedIds} // ✅ PASS HERE
            />
        </div>
    );
}

function getAvgRating(post) {
    const feedbacks = post.feedbacks || [];

    if (!feedbacks.length) return 0;

    return Number(
        (
            feedbacks.reduce((sum, f) => sum + (f.rating || 0), 0) /
            feedbacks.length
        ).toFixed(1)
    );
}

function applyFilters(posts, category, allergy) {
    const categories = category ? category.split(",").filter(Boolean) : [];
    const allergies = allergy ? allergy.split(",").filter(Boolean) : [];

    return posts.filter((post) => {
        const postCategories = post.categories ?? [];
        const postAllergies = post.allergies ?? [];

        const categoryMatch =
            categories.length === 0 ||
            categories.some((c) => postCategories.includes(c));

        // ✅ NORMALISE EVERYTHING TO STRINGS
        const postAllergySet = new Set(
            postAllergies.map(a =>
                typeof a === "object" ? a.name : a
            )
        );

        const selectedAllergies = allergies.map(a => a.toLowerCase());

        const allergyMatch =
            selectedAllergies.length === 0 ||
            !selectedAllergies.some(a =>
                postAllergySet.has(a.toLowerCase())
            );

        return categoryMatch && allergyMatch;
    });
}

function SearchResults({ data, query, navigate, params, allPosts, blacklistedIds }) {
    const sortedPosts = useMemo(() => {
        const filtered = applyFilters(
            data.posts,
            params.get("category") || "",
            params.get("allergy") || ""
        );

        // ✅ FILTER OUT BLACKLISTED USERS
        const withoutBlacklisted = filtered.filter(
            (post) => !blacklistedIds.includes(post.authorId)
        );

        return [...withoutBlacklisted].sort(
            (a, b) => getAvgRating(b) - getAvgRating(a)
        );
    }, [data.posts, params, blacklistedIds]);

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
                allPosts={allPosts}
            />
        </>
    );
}

function ResultSection({ title, items, type, query, navigate, allPosts }) {
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
                        allPosts={allPosts}
                    />
                ))}
            </div>
        </div>
    );
}

function ResultCard({ item, type, query, navigate, allPosts = [] }) {
    function highlight(text, q) {
        return highlightParts(text, q).map((part, i) =>
            part.match ? (
                <span key={i} className="text-accent font-semibold">
                    {part.text}
                </span>
            ) : (
                part.text
            )
        );
    }

    if (type === "post") {
        const avg = getAvgRating(item);

        const isEdited =
            item.updateDate && item.creationDate
                ? item.updateDate !== item.creationDate
                : false;

        return (
            <div
                onClick={() => navigate(`/post/${item.id}`)}
                className="p-4 rounded-2xl bg-white dark:bg-gray-900 shadow-soft hover:shadow-md transition cursor-pointer space-y-2"
            >
                <div className="font-semibold">{highlight(item.title, query)}</div>

                <div className="text-sm text-gray-500">
                    by {item.author}
                </div>

                <div className="flex items-center gap-2">
                    {avg ? (
                        <span className="text-sm px-3 py-1 rounded-full bg-yellow-100 dark:bg-yellow-900 text-yellow-700 dark:text-yellow-300">
                            {avg} ★
                        </span>
                    ) : (
                        <span className="text-sm text-gray-400">No ratings</span>
                    )}
                </div>

                <div className="flex flex-wrap gap-2">
                    {item.categories?.map((c) => (
                        <span key={c} className="text-xs px-2 py-1 rounded-full bg-blue-100 dark:bg-blue-900 text-blue-700 dark:text-blue-300">
                            {c}
                        </span>
                    ))}

                    {item.allergies?.map((a) => (
                        <span key={a} className="text-xs px-2 py-1 rounded-full bg-red-100 dark:bg-red-900 text-red-700 dark:text-red-300">
                            {a}
                        </span>
                    ))}
                </div>

                <div className="text-xs text-gray-500 flex gap-2">
                    <span>Created: {item.creationDate}</span>
                    {isEdited && <span>(edited)</span>}
                </div>
            </div>
        );
    }

    const userPosts = allPosts.filter((p) => p.authorId === item.id);

    const now = Date.now();

    const ratings = userPosts.flatMap((p) => {
        const postTime = new Date(p.creationDate).getTime();
        const weight = 1 / (1 + (now - postTime) / (1000 * 60 * 60 * 24 * 30));

        return (p.feedbacks || []).map((f) => ({
            rating: f.rating || 0,
            weight,
        }));
    });

    let reputation = 0;

    if (ratings.length) {
        const weightedSum = ratings.reduce((sum, r) => sum + r.rating * r.weight, 0);
        const weightSum = ratings.reduce((sum, r) => sum + r.weight, 0);

        const avg = weightedSum / weightSum;
        const diff = avg - 2.5;

        reputation = Math.round(diff * ratings.length);
    }

    let badge = getBadge(reputation);

    return (
        <div
            onClick={() => navigate(`/user/${item.id}`)}
            className="p-4 rounded-2xl bg-white dark:bg-gray-900 shadow-soft hover:shadow-md transition cursor-pointer space-y-1"
        >
            <div className="font-semibold">{highlight(item.login, query)}</div>

            <div className="inline-flex w-fit text-xs px-2 py-1 rounded-full 
            text-green-700 dark:text-green-300 bg-green-100 dark:bg-green-900
            border border-green-200 dark:border-green-300">
                {badge}
            </div>

            <div className="text-xs text-gray-500">
                Reputation: {reputation}
            </div>
        </div>
    );
}