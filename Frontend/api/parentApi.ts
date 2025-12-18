import { ParentData } from '@/types/parent'
import {ChildData} from "@/types/child";
import {getAllChildrenData} from "@/api/childApi";

const API_BASE_URL = 'http://localhost:8080/api';


export async function getAllParentsData() {
    try{
        const response = await fetch(API_BASE_URL + '/parents');

        if (!response.ok) {
            throw new Error(`Error: ${response.status}`);
        }
        const data = await response.json();
        console.log("Parents: ", data);

        return data.map((parent:any )=>( {
            id: parent.id,
            name:parent.name,
            phoneNumber: parent.phoneNumber,
            email:parent.email,
            password:parent.password,
            address:parent.address,
            imageUri:parent.imageUri,
            childIds:parent.childIds
        } as ParentData));
    }
    catch(error){
        console.log('Error in getting parents: ', error);
        return [];
    }


}
export async function getParentById(id: string){
    try {
        const response = await fetch(`${API_BASE_URL}/parents/${id}`);

        if (!response.ok) {
            throw new Error(`Error in getting parent by ID: ${response.status}`);
        }

        const parent = await response.json();
        console.log("ParentProfile by ID: ", parent);

        const childIds = parent.children
            ? parent.children.map((child: any) => child.childId)
            : [];

        console.log("Extracted childIds:", childIds);

        return {
            id: parent.id,
            name: parent.name,
            phoneNumber: parent.phoneNumber,
            email: parent.email || '',
            password: parent.password || '',
            address: parent.address || '',
            imageUri: parent.imageUri || undefined,
            childIds: childIds
        };
    } catch (e) {
        console.log("Error getting parent by id: ", e);
        return null;
    }
}

export async function getParentsByIds(parentsIds: number[]): Promise<ParentData[]> {
    try {
        if (!parentsIds || parentsIds.length === 0) {
            return [];
        }
        const allParents = await getAllParentsData();
        return allParents.filter((parent: ParentData) => parentsIds.includes(parent.id));
    } catch (error) {
        console.log("Error in getting parents by ids", error);
        return [];
    }
}



