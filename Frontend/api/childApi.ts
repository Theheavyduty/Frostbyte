import {ChildData} from "@/types/child";

const API_BASE_URL = 'http://localhost:8080/api';

export async function getAllChildrenData() {
    try{
        const response = await fetch(API_BASE_URL + '/children');

        if (!response.ok) {
            throw new Error(`Error: ${response.status}`);
        }
        const data = await response.json();
        console.log("Children: ", data);

        return data.map((child:any )=>( {
            id: child.id,
            name:child.name,
            department: child.department,
            additionalNotes:child.additionalNotes,
            birthday: child.birthday,
            parentsIds: child.parentIds,
        } as ChildData));
    }
    catch(error){
        console.log('Error in getting all children: ', error);
        return [];
    }
}

export async function getChildById(id: string){
    try {
        const response = await fetch(`${API_BASE_URL}/children/${id}`);

        if (!response.ok) {
            throw new Error(`Error in getting child by ID: ${response.status}`);
        }

        const child = await response.json();

        const parentsIds = child.parents
            ? child.parents.map((parent: any) => parent.parentId)
            : [];

        return {
            id: child.id,
            name: child.name || '',
            department: child.department || '',
            birthday: child.birthday || '',
            additionalNotes: child.additionalNotes || '',
            parentsIds: parentsIds,
        };
    } catch (error) {
        console.log("Error getting child by id: ", error);
        return null;
    }
}
export async function getChildrenByIds(childIds: number[]): Promise<ChildData[]> {
    try {
        if (!childIds || childIds.length === 0) {
            return [];
        }
        const allChildren = await getAllChildrenData();
        return allChildren.filter((child: ChildData) => childIds.includes(child.id));
    } catch (error) {
        console.log("Error in getting children by ids", error);
        return [];
    }
}
