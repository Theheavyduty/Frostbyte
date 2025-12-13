import {Tabs} from 'expo-router';
import React from 'react';
import {HapticTab} from '@/components/haptic-tab';
import {Colors} from '@/constants/theme';
import {useColorScheme} from '@/hooks/use-color-scheme';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import Ionicons from '@expo/vector-icons/Ionicons';


export default function TabLayout() {
    const colorScheme = useColorScheme();

    return (
        <Tabs
            screenOptions={{
                tabBarActiveTintColor: Colors[colorScheme ?? 'light'].tint,
                headerShown: false,
                tabBarButton: HapticTab,
            }}>
            <Tabs.Screen
                name="employee"
                options={{
                    title: 'Profil',
                    tabBarIcon: ({color}) => <MaterialIcons name="person" size={24} color={color}/>,
                }}
            />
            <Tabs.Screen
                name="index"
                options={{
                    title: 'Krysseliste',
                    tabBarIcon: ({color}) => <Ionicons name="checkmark-circle-outline" size={24} color="black"/>,
                }}
            />
            <Tabs.Screen
                name="catalogue"
                options={{
                    title: 'Katalog',
                    tabBarIcon: ({color}) => <MaterialIcons name="groups" size={24} color={color}/>,
                }}
            />
        </Tabs>
    );
}
