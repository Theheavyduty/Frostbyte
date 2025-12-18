import {Tabs} from 'expo-router';
import React from 'react';
import {HapticTab} from '@/components/haptic-tab';
import {Colors} from '@/constants/theme';
import {useColorScheme} from '@/hooks/use-color-scheme';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import Ionicons from '@expo/vector-icons/Ionicons';
import {View} from "react-native";


// NavBar



function ActiveIcon ({ children }: { children: React.ReactNode }){
    return (
        <View
        style={{
            borderStyle: "solid",
            width: 45,
            height: 45,
            borderWidth: 6,
            borderColor: "#FFFFFF",
            padding: 25,
            borderRadius: 40,
            justifyContent: 'center',
            alignItems: 'center',
            shadowColor: '#FFFFFF',
            shadowOpacity: 1,
            shadowRadius: 10,
            shadowOffset: { width: 0, height: 2 },

            elevation: 8,
        }} >
            {children}

        </View>
    );
}
export default function TabLayout() {
    const colorScheme = useColorScheme();

    return (
        <Tabs
            screenOptions={{
                headerShown: false,
                // tabBarButton: HapticTab,
                tabBarStyle: {
                    backgroundColor: '#91CEFF',
                    borderTopWidth: 0,
                    height: 105,
                    paddingBottom: 5,
                    paddingTop: 25,
                },

                tabBarActiveTintColor: undefined,
                tabBarInactiveTintColor: undefined,

                tabBarLabelStyle: {}

            }}>


            <Tabs.Screen
                name="employee"
                options={{
                    title: 'Profil',
                    tabBarIcon: ({focused}) =>
                        focused ? (<ActiveIcon>
                            <MaterialIcons name="person" size={45} color={'#011638'} height={45} width={45} style={{marginBottom: 12, paddingBottom: 11}}/>
                        </ActiveIcon>) : (
                            <MaterialIcons name="person" size={45} color={'#011638'} height={45} width={45} style={{marginBottom: 12, paddingBottom: 11}}/>
                        ),
                }}
            />
            <Tabs.Screen
                name="index"
                options={{
                    title: 'Krysseliste',
                    tabBarIcon: ({focused})  =>
                    focused ? (<ActiveIcon>
                        <Ionicons name="checkmark-circle-outline" size={45} color={'#011638'} height={45} width={45} style={{marginBottom: 12, paddingBottom: 11}}  />
                    </ActiveIcon>) : (
                        <Ionicons name="checkmark-circle-outline" size={45} color={'#011638'} height={45} width={45} style={{marginBottom: 12, paddingBottom: 11}}  />
                    ),

                }}
            />

            <Tabs.Screen
                name="catalogue"
                options={{
                    title: 'Katalog',
                    tabBarIcon: ({focused}) =>
                        focused ? (<ActiveIcon>
                                <MaterialIcons name="groups" size={42} color={'#011638'} height={42} width={42} style={{marginBottom: 12, paddingBottom: 11}}/>
                        </ActiveIcon>) : (
                            <MaterialIcons name="groups" size={42} color={'#011638'} height={42} width={42} style={{marginBottom: 12, paddingBottom: 11}}/>
                        ),
                }}
            />
        </Tabs>

    );
}
