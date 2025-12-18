
import React, {useEffect, useState} from "react";
//import { Passkey } from 'react-native-passkey';
import {LinearGradient} from "expo-linear-gradient";
import {getTimeOfDay, timeThemes} from "@/utils/timeUtils";
import {Alert, StyleSheet, Text, TextInput, TouchableOpacity, View} from "react-native";
import { useAuth } from "@/contexts/AuthContext";
import { router } from "expo-router"



export default function Index() {
    const [timeOfDay, setTimeOfDay] = useState<"morning" | "noon" | "evening" >("morning")
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("")
    const { login } = useAuth();


    const handleUserNamePasswordLogin= async () => {
        setIsLoading(true)

        try {
            const formData = new URLSearchParams();
            formData.append("username", username);
            formData.append("password", password);


            const res = await fetch("http://localhost:8000/login", {
                method: "POST",
                headers: {"Content-Type": "application/x-www-form-urlencoded"},
                body: formData.toString(),
                credentials: "include",
                redirect: "manual",
            })

            if (res.ok) {
                await login(username)
                router.replace("/(tabs)")
                console.log(res + "logged in successfully")
            } else{
                const errorText = await res.text()
                console.log("login failed", errorText)
                Alert.alert("Oisånn!","Feil brukernavn eller passord, prøv igjen.")
                setIsLoggedIn(false)
                setUsername("")
                setPassword("")
            }

        } catch (e) {
            console.log("Error logging in user" + e)
        } finally {
            setIsLoading(false)
        }

    }

    useEffect(() => {
        let date = new Date();
        const hour = date.getHours();
        setTimeOfDay(getTimeOfDay(hour))

    }, []);

    const theme = timeThemes[timeOfDay]

    return (
        <LinearGradient colors={theme.gradient} style={styles.container}>
            <View style={styles.contentContainer}>
                <View style={styles.titleContainer}>
                    <Text
                        style={{fontSize: 100}}>{theme.emoji}</Text>
                    <Text style={{textAlign: "center", color:theme.messageColor, fontSize: 32, fontWeight: "bold"}}>{theme.message}</Text>


                </View>


                <View style={styles.loginFields}>

                    <TextInput
                        style={styles.inputField}
                        onChangeText={setUsername}
                        value={username}
                        placeholder="Brukernavn"
                        placeholderTextColor="gray"
                        editable={!isLoading}
                        autoCapitalize="none"
                        returnKeyType="next"


                    />
                    <TextInput
                        style={styles.inputField}
                        onChangeText={setPassword}
                        value={password}
                        placeholder="Passord"
                        placeholderTextColor="gray"
                        editable={!isLoading}
                        secureTextEntry={true}
                        textContentType={"password"}
                        returnKeyType="go"


                    />

                    <TouchableOpacity
                        onPress={() => handleUserNamePasswordLogin()}
                        style={[styles.button,{backgroundColor: theme.buttonColor}]}>

                        <Text style={{color: theme.buttonTextColor,  fontWeight: 'normal',
                            fontFamily: 'Inter', fontSize: 20}}>
                            Logg inn
                        </Text>
                    </TouchableOpacity>


                </View>

            </View>
        </LinearGradient>
    )
}

const styles = StyleSheet.create({
        container: {
            flex: 1,
            alignItems:'center',
            paddingTop:10,
            paddingBottom:10,
            paddingHorizontal:20,
        },
        contentContainer: {
            flex: 1,
            alignItems: 'center',
            justifyContent: 'center',

        },
        titleContainer: {
            flex: 1,
            flexDirection: 'column',
            justifyContent:'center',
            alignItems:'center',
            width:'60%',


        },
        button: {
            width:'100%',
            borderRadius: 40,
            paddingVertical: 16,
            paddingHorizontal: 60,
            marginBottom: 100


        },
        loginFields:{
            justifyContent:'center',
            alignItems:'center',
        },
        inputField: {
            backgroundColor: 'rgba(255,255,255,0.7)',
            paddingVertical: 15,
            paddingHorizontal: 15,
            marginBottom: 15,
            width: 240,
            borderRadius: 8,

        }

    }

)