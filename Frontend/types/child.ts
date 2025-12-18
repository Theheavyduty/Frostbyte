import {Department} from "@/types/department";

export interface ChildData{
    id: number,
    name: string,
    birthday: string,
    department: Department,
    additionalNotes:string,
    parentsIds:number[]
}

export type ChildStatus = {
    childId: number,
    status: Status;
    eventTime: Date,
}

export type ChildWithStatus = ChildData & {
    status: Status;
    updatedAt?: Date;
}

export type Status = "LEVERT" | "HENTET" | "SYK" | "FRAVAER";
export type FilterStatus = Status | "ALLE";
