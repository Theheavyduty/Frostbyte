import React, {createContext, useContext, useEffect, useState} from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";


interface AuthContextType {
    isAuthenticated: boolean;
    isLoading: boolean;
    username: string | null
    login: (username: string) => Promise<void>
    logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextType |  undefined>(undefined);

export const AuthProvider = ({ children }: {  children: React.ReactNode } ) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [username, setUsername] = useState<string | null>(null);


    useEffect(() => {
        checkAuth();
    },[]);



    const checkAuth = async () => {
        try {
            const userNameInStorage = await AsyncStorage.getItem("userName");
            if (userNameInStorage) {
                setIsAuthenticated(true);
                setUsername(userNameInStorage)
            }
        } catch (error){
            console.log("Error checking username in storage", error);
        } finally {
            setIsLoading(false);
        }


    }

    const login = async (user: string) => {
        try {
            await AsyncStorage.setItem("userName", user);
            setIsAuthenticated(true);
            setUsername(user);
        } catch (error){
            console.log("Error storing username in session", error);
        }
    }

    const logout = async () => {
        try {
            await AsyncStorage.removeItem("userName");
        } catch (error) {
            console.log("Error removing username from session storage", error);
        }
    }


    return (
        <AuthContext.Provider value={{isAuthenticated, isLoading, username, login, logout}}>
            {children}
        </AuthContext.Provider>
    )


}

export const useAuth = () =>{
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within a auth");
    }
    return context;
}