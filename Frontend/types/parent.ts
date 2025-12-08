export interface ParentData {
    id: string, // any? We need to represent ID as any or string.
    name: string,
    email:string,
    password: string,
    phoneNumber: number,
    address: string,
    imageUri?:string,
    // TODO add connection to children.
}
