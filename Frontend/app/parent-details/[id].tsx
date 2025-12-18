
import {router, useLocalSearchParams} from "expo-router";
import React, { useEffect, useState } from "react";
import {
    Animated,
    FlatList,
    Image,
    Pressable,
    StyleSheet,
    Text,
    TextInput,
    View,
} from "react-native";
import {ParentData} from "@/types/parent";
import { getParentById} from "@/api/parentApi";
import {LinearGradient} from "expo-linear-gradient";
import Child from "@/components/Child";
import {ChildData} from "@/types/child";
import {getChildrenByIds} from "@/api/childApi";
import AntDesign from "@expo/vector-icons/AntDesign";
import ScrollView = Animated.ScrollView;
import Parent from "@/components/Parent";

// How to add navigation bar here?
export default function ParentDetailsPage() {
    const { id } = useLocalSearchParams<{ id: string }>();
    const [parent, setParent] = useState<ParentData | null>(null);
    const [children, setChildren] = useState<ChildData[]>([]);
    const [isRefreshing,setIsRefreshing] = useState(false);


    const loadData = async () => {
        try {
            setIsRefreshing(true);
            console.log("Fetching parent with ID:", id);
            console.log(children);

            const parentById = await getParentById(id);
            console.log("Received parent data:", parentById);


            if (!parentById) {
                setParent(null);
                setChildren([]);
                return;
            }

            setParent(parentById);

            if (parentById.childIds && parentById.childIds.length > 0) {
                console.log("Fetching children with IDs:", parentById.childIds);
                const childrenData = await getChildrenByIds(parentById.childIds);
                console.log("Received children data:", childrenData);
                setChildren(childrenData);
            } else {
                setChildren([]);
            }

        } catch (error) {
            console.log('Error loading data:', error);
        } finally {
            setIsRefreshing(false);
        }
    };

    useEffect(() => {
        loadData();
    }, [id]);

    if (isRefreshing) {
        return (
            <View style={{ flex: 1, justifyContent: "center", alignItems: "center", backgroundColor: 'white' }}>
                <Text>Henter innlegg...</Text>
            </View>
        );
    }

    if (!parent) {
        return (
            <View style={{ flex: 1, justifyContent: "center", alignItems: "center", backgroundColor: 'white' }}>
                <Text>Ingen data funnet</Text>
            </View>
        );
    }

    return (
        <LinearGradient
            colors={['#b2c9ff', '#ffffff']}
            locations={[0, 0.12]}
            start={{ x: 0.5, y: 0 }}
            end={{ x: 0.5, y: 1 }}
            style={styles.mainContainer}
        ><View style={styles.titleContainer}>
            <Pressable
                onPress={() => router.back()}
            >
                <AntDesign name="arrow-left" size={20} color="black" />
            </Pressable>
            <Text style={styles.title}>Foresattprofil</Text>
        </View>
            <ScrollView
                style={styles.scrollView}
                contentContainerStyle={styles.contentContainer}
                showsVerticalScrollIndicator={false}
            >
                <Text style={{fontSize:100}}>🧑‍🦱</Text>
                <Text style={styles.biggerText}>
                    {parent.name}
                </Text>
                <View style={styles.infoContainer}>
                    <Text style={styles.mediumText}>Kontaktinfo:</Text>
                    <Text style={styles.lighterText}>
                        Telefonnummer: {parent.phoneNumber}
                    </Text>
                    <Text style={styles.lighterText} >
                        Epost: {parent.email}
                    </Text>
                    <Text style={styles.lighterText}>
                        Addresse: {parent.address}
                    </Text>
                </View>
                <View style={styles.listContainer}>
                    <Text style={styles.mediumText}>Barn</Text>
                    {children.map((child) => (
                        <Child
                            key={child.id}
                            childData={child}
                            customStyle={{ backgroundColor: '#e6f0ff' }}
                        />
                    ))}

                </View>
                <View style={styles.buttonContainer}>
                    <Pressable style={styles.button}>
                        <Text style={styles.text}>Rediger</Text>
                    </Pressable>
                    <Pressable style={[styles.button,styles.secondaryButton]}>
                        <Text style={styles.text}>Slett bruker</Text>
                    </Pressable>
                </View>
</ScrollView>
        </LinearGradient>
    );
}

const styles = StyleSheet.create({
    mainContainer: {
        flex: 1,
        width: '100%',
    },
    scrollView: {
        flex: 1,
    },
    contentContainer: {
        alignItems: 'center',
        paddingTop: 10,
        paddingBottom: 10,
        paddingHorizontal: 20,
    },
    titleContainer: {
        flexDirection: 'row',
        alignItems:'center',
        justifyContent: 'space-between',
        padding:20,
        marginTop:55
    },
    title:{
        fontSize:20,
        fontWeight:'600',
        flex: 1,
        textAlign: 'center'
    },

    biggerText:{
        fontSize: 28,
        fontWeight:'400',
        margin:2
    },
    mediumText:{
        fontSize: 16,
        fontWeight:'400',
        margin:5
    },
    text: {
        fontSize: 16,
        fontWeight:'300',
        margin:2
    },

    lighterText:{
        fontSize:16,
        fontWeight:'300',
        margin:2
    },
    listContainer:{
        marginVertical:10,
        width:'100%'
    },
    infoContainer:{
        width:'100%',
        marginTop:30,
        marginBottom:20,
        borderRadius:10,
        paddingHorizontal:10,
        paddingVertical:20,
        backgroundColor:'#e6f0ff',
        shadowColor: 'gray',
        shadowRadius: 10,
        shadowOpacity: 0.15,
        shadowOffset: { width: 0, height: 4 }
    },
    buttonContainer:{
        flexDirection:'row',
        alignItems:'center',
        justifyContent:'space-between',
        gap:20,
        marginVertical:20,
    },
    button: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        minWidth:120, // How to make it responsive?
        maxWidth:200,
        backgroundColor: '#B8C8FF',
        paddingHorizontal: 5,
        paddingVertical: 10,
        borderRadius: 50,
        borderWidth: 2,
        borderColor: '#B8C8FF',
        shadowColor: 'gray',
        shadowRadius: 10,
        shadowOpacity: 0.15,
        shadowOffset: { width: 0, height: 4 }
    },
    secondaryButton:{
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        minWidth:120, // How to make it responsive?
        maxWidth:200,
        backgroundColor: 'white',
        paddingHorizontal: 5,
        paddingVertical: 10,
        borderRadius: 50,
        borderWidth: 2,
        borderColor: '#B8C8FF',
        shadowColor: 'gray',
        shadowRadius: 10,
        shadowOpacity: 0.15,
        shadowOffset: { width: 0, height: 4 }
    }
});