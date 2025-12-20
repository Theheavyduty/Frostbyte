import {ChildData, ChildStatus, Status} from "@/types/child";
import { getAllChildrenData } from "@/api/childApi";

const API_BASE_STATUS_URL = 'http://localhost:8080/api/child-status';

export async function getStatus(childId: number): Promise<ChildStatus | null> {
    try {
        const response = await fetch(API_BASE_STATUS_URL + `/children/${childId}/latest`);

        if (!response.ok) {
            throw new Error(`Error: ${response.status}`);
        }
        const data = await response.json();
        console.log("Status response: ", data);
        return {
            childId: data.childId,
            status: data.status,
            eventTime: new Date(data.eventTime)
        } as ChildStatus;
    } catch (e) {
        console.log(API_BASE_STATUS_URL + `/children/${childId}/latest`);
        console.error('Error in getting status: ', e)
        return null;
    }
}

export async function registerStatusApi(childId: number, status: Status, employeeId: number) {
    try {
        const response = await fetch(API_BASE_STATUS_URL + `/checks`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({childId, status, employeeId})
        });

        if (!response.ok) throw new Error(`Error: ${response.status}`);

        return await response.json();
    } catch (error) {
        console.error('Error in registerStatus: ', error);
        return null;
    }
}

export async function registerSyk(childId: number, employeeId: number, symptoms: string) {
    try {
        const response = await fetch(API_BASE_STATUS_URL + `/syk`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({childId, employeeId, sicknessTime: new Date().toISOString(), symptoms})
        });

        if (!response.ok) throw new Error(`Error: ${response.status}`);

        return await response.json();
    } catch (error) {
        console.error('Error in registerSyk: ', error);
        return null;
    }
}

export async function registerFravaer(childId: number, employeeId: number, reason: string) {
    try {
        const response = await fetch(API_BASE_STATUS_URL + `/fravaer`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({childId, employeeId, absenceReasons: reason})
        });

        if (!response.ok) throw new Error(`Error: ${response.status}`);

        return await response.json();
    } catch (error) {
        console.error('Error in registerFravaer: ', error);
        return null;
    }
}

export async function getChildrenBySearch(searchTerm: string): Promise<ChildData[]> {
    try {
        const allChildren = await getAllChildrenData();
        // Before it said just child.
        return allChildren.filter((child: { name: string; }) => child.name.toLowerCase().includes(searchTerm.toLowerCase()));
    } catch(error) {
        console.error("There was an error with the search", error);
        return [];
    }
}