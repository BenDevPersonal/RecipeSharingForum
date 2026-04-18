export function computeReputation(allPosts, userId) {
    const userPosts = allPosts.filter((p) => p.authorId === userId);

    const now = Date.now();

    const ratings = userPosts.flatMap((p) => {
        const postTime = new Date(p.creationDate).getTime();

        const weight =
            1 / (1 + (now - postTime) / (1000 * 60 * 60 * 24 * 30));

        return (p.feedbacks || []).map((f) => ({
            rating: f.rating || 0,
            weight,
        }));
    });

    let reputation = 0;
    let avg = 0;

    if (ratings.length) {
        const weightedSum = ratings.reduce(
            (sum, r) => sum + r.rating * r.weight,
            0
        );

        const weightSum = ratings.reduce(
            (sum, r) => sum + r.weight,
            0
        );

        avg = weightedSum / weightSum;

        const diff = avg - 2.5;

        reputation = Math.round(diff * ratings.length);
    }

    return { reputation, avg };
}

export function getBadge(reputation) {
    if (reputation > 50) return "Chef Legend 👑";
    if (reputation > 20) return "Master Cook 🔥";
    if (reputation > 5) return "Skilled Cook 👨‍🍳";
    if (reputation >= 1) return "Home Cook";
    if (reputation === 0) return "Newbie";
    return "Burnt Toast 💀";
}