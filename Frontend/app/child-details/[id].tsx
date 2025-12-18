
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
import {getParentById, getParentsByIds} from "@/api/parentApi";
import {ChildData} from "@/types/child";
import {getChildById, getChildrenByIds} from "@/api/childApi";
import {LinearGradient} from "expo-linear-gradient";
import Child from "@/components/Child";
import Parent from "@/components/Parent";
import AntDesign from "@expo/vector-icons/AntDesign";
import ScrollView = Animated.ScrollView;
import FontAwesome from "@expo/vector-icons/FontAwesome";

export default function ChildDetailsPage() {
    const { id } = useLocalSearchParams<{ id: string }>();
    const [child, setChild] = useState<ChildData | null>(null);
    const [parents, setParents] = useState<ParentData[]>([]);
    const [isRefreshing,setIsRefreshing] = useState(false);

    const loadData = async () => {
        try {
            setIsRefreshing(true);
            const childById = await getChildById(id);

            if (!childById) {
                setChild(null);
                setParents([]);
                //setEmployers
                return;
            }

            setChild(childById);

            if (childById.parentsIds && childById.parentsIds.length > 0) {
                const parentData = await getParentsByIds(childById.parentsIds);
                setParents(parentData);
            } else {
                setParents([]);
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

    if (!child) {
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
        >
                <View style={styles.titleContainer}>
            <Pressable
                onPress={() => router.back()}
            >
                <AntDesign name="arrow-left" size={20} color="black" />
            </Pressable>
            <Text style={styles.title}>Barneprofil</Text>
        </View>
            <ScrollView
                style={styles.scrollView}
                contentContainerStyle={styles.contentContainer}
                showsVerticalScrollIndicator={false}
            >
                <Text style={{fontSize:100}}>👶</Text>
                <Text style={styles.biggerText}>
                    {child.name}
                </Text>
                <View style={styles.infoContainer}>
                    <Text style={styles.mediumText}>Informasjon:</Text>
                    <Text style={styles.lighterText}>
                        Fødelsedato: {child.birthday}
                    </Text>
                    <Text style={styles.lighterText}>Avdeling: {child.department.name}</Text>
                    <Text style={styles.lighterText}>
                        Tilleginformasjon: {child.additionalNotes}
                    </Text>
                </View>
                <View style={styles.listContainer}>
                    <Text style={styles.mediumText}>Registrerte foresatte</Text>
                    {parents.map((parent) => (
                        <Parent
                            key={parent.id}
                            parentData={parent}
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
        fontWeight:'600',
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
    },
    itemContainer: {
        flexDirection: 'row',
        backgroundColor:'#e6f0ff',
        width:'100%',
        borderRadius:10,
        paddingVertical:16,
        paddingHorizontal:16,
        justifyContent:'space-between',
        alignItems:'center',
        marginTop:10,
        shadowColor: 'gray',
        shadowRadius: 10,
        shadowOpacity: 0.15,
        shadowOffset: { width: 0, height: 4 }
    },
});
