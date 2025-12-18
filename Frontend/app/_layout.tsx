import { DarkTheme, DefaultTheme, ThemeProvider } from '@react-navigation/native';
import { Stack } from 'expo-router';
import { StatusBar } from 'expo-status-bar';
import 'react-native-reanimated';

import { useColorScheme } from '@/hooks/use-color-scheme';
import {AuthProvider} from "@/contexts/AuthContext";


export default function RootLayout() {
  const colorScheme = useColorScheme();
    // This page need authentification provider and more screens.
  return (
    <ThemeProvider value={colorScheme === 'dark' ? DarkTheme : DefaultTheme}>
      <AuthProvider>
        <Stack screenOptions={{ headerShown: false }}>
            <Stack.Screen name="(auth)" options={{ headerShown: false }} />
        <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
        <Stack.Screen name="+not-found" />
        {/*<Stack.Screen name="child-details/[id]" options={{ headerShown: false }}/>
            <Stack.Screen name="parent-details/[id]" options={{ headerShown: false }}/>*/}


      </Stack>
    </AuthProvider>
    </ThemeProvider>
  );
}
