export interface ParentData {
    id: number,
    name: string,
    email: string,
    password: string,
    phoneNumber: number,
    address: string,
    imageUri?: string,
    childIds?: number[],
}