export function getTimeOfDay(hour: number): "morning" | "noon" | "evening" {
    if (hour >= 5 && hour < 12) return "morning";
    if (hour >= 12 && hour < 17) return "noon";
    return "evening";
}

export const timeThemes= {
    morning: {
        gradient: ['#B2C9FF','#FAF2A1'] as const,
        message: "God Morgen!",
        messageColor: "#011638",
        buttonColor: "#011638",
        buttonTextColor: "#ffffff",
        pinButtonColor: "#ffffff",
        emoji: "☀️"
    },
    noon: {
        gradient: ['#B2C9FF', '#FFFFFF'] as const,
        message: "God Ettermiddag!",
        messageColor: "#011638",
        buttonColor: "#011638",
        buttonTextColor: "#ffffff",
        pinButtonColor: "#011638",
        emoji: "☀️"
    },
    evening: {

        gradient: ['#011638', '#FFFFFF'] as const,
        message: "God Kveld!",
        messageColor: "#011638",
        buttonColor: "#011638",
        buttonTextColor: "#ffffff",
        pinButtonColor: "#011638",
        emoji: "🌙"

    },
}