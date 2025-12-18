import {FilterStatus, Status} from "@/types/child";

export const statusToLabel: Record<Status, string> = {
    LEVERT: "Levert",
    HENTET: "Hentet",
    SYK: "Syk",
    FRAVAER: "Fravær",
};

export const labelToStatus: Record<string, Status> = {
    Levert: "LEVERT",
    Hentet: "HENTET",
    Syk: "SYK",
    Fravær: "FRAVAER",
};
export const filterStatusToLabel: Record<FilterStatus, string> = {
    ...statusToLabel,
    ALLE: "Alle",
};

export const labelToFilterStatus: Record<string, FilterStatus> = {
    ...labelToStatus,
    Alle: "ALLE",
};