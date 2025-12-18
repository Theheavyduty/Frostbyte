import { Image } from 'expo-image';
import {
    Platform,
    StyleSheet,
    View,
    Text,
    Animated,
    FlatList,
    TextInput,
    TouchableOpacity,
    Modal,
    Pressable,
    ScrollView
} from 'react-native';
import FontAwesome from '@expo/vector-icons/FontAwesome';
import { LinearGradient } from 'expo-linear-gradient';
import AntDesign from '@expo/vector-icons/AntDesign';
import React, {useEffect, useRef, useState} from "react";
import {ParentData} from "@/types/parent";
import {getAllParentsData} from "@/api/parentApi";
import {DropDownMenu} from "@/components/DropDownMenu";
import Parent from "@/components/Parent";
import {Picker} from "@react-native-picker/picker";
import {getAllChildrenData} from "@/api/childApi";
import {ChildData} from "@/types/child";
import Child from "@/components/Child";
import EvilIcons from "@expo/vector-icons/EvilIcons";


export default function CataloguePage() {
    const [searchText, setSearchText] = React.useState("");
    const [parents, setParents] = useState<ParentData[]>([]);
    const [filteredParents, setFilteredParents] = useState<ParentData[]>([]);
    const [filteredChildren, setFilteredChildren] = useState<ChildData[]>([]);
    const [children, setChildren] = useState<ChildData[]>([]);
    const [isRefreshing,setIsRefreshing] = useState(false);
    const [activeTab, setActiveTab] = useState<'children' | 'parents'>('children');
    const [selectedDepartment, setSelectedDepartment] = useState<string | number>('all');

    useEffect(()=>{
        fetchParents();
        fetchChildren();
    },[])

    async function fetchParents(){
        setIsRefreshing(true);
        try{
            const allParents = await getAllParentsData()
            setParents(allParents)
            setFilteredParents(allParents);
        }
        catch(error){
            console.log("Error in fetching parents",error);
        }
        finally {
            setIsRefreshing(false);
        }
    }
    async function fetchChildren(){
        setIsRefreshing(true);
        try{
            const allChildren = await getAllChildrenData()
            setChildren(allChildren)
            setFilteredChildren(allChildren);
        }
        catch(error){
            console.log("Error in fetching children",error);
        }
        finally {
            setIsRefreshing(false);
        }
    }
    const handleRefresh = () => {
        if (activeTab === 'children') {
            fetchChildren();
        } else {
            fetchParents();
        }
    };


    const handleSearch = async (text: string) => {
        setSearchText(text);

        if (!text.trim()) {
            setFilteredParents(parents);
            setFilteredChildren(children);
            return;
        }

        const query = text.toLowerCase();

        if (activeTab === 'parents') {
            const results = parents.filter(parent =>
                parent.name.toLowerCase().includes(query)
            );
            setFilteredParents(results);
        } else {
            const results = children.filter(child =>
                child.name.toLowerCase().includes(query)
            );
            setFilteredChildren(results);
        }
    };



    return (
        <LinearGradient
            colors={['#b2c9ff', '#ffffff']} // gradient colors
            style={styles.mainContainer}
            start={{ x: 0.5, y: 0 }} // gradient start point
            end={{ x: 0.5, y: 1 }}   // gradient end point
        >
            <View style={styles.mainContainer}>
                <View style={styles.titleContainer}>
                    <Text style={styles.title}>Katalog</Text>
                </View>
                <View style={styles.tabContainer}>
                    <TouchableOpacity
                        style={[
                            styles.tabButton,
                            activeTab === 'children' && styles.activeTabButton
                        ]}
                        onPress={() => setActiveTab('children')}
                    >
                        <Text style={[
                            styles.text,
                            activeTab === 'children' && styles.text
                        ]}>Barn</Text>
                    </TouchableOpacity>

                    <TouchableOpacity
                        style={[
                            styles.tabButton,
                            activeTab === 'parents' && styles.activeTabButton
                        ]}
                        onPress={() => setActiveTab('parents')}
                    >
                        <Text style={[
                            styles.text,
                            activeTab === 'parents' && styles.text
                        ]}>Foreldre</Text>
                    </TouchableOpacity>
                </View>
                <View style={styles.searchContainer}>
                    <TextInput
                        placeholder={"Søk etter navn"}
                        style={styles.inputContainer}
                        value={searchText}
                        onChangeText={handleSearch}
                    />
                    <EvilIcons name="search" size={24} color="gray" />

                </View>



                <View style={styles.listContainer}>
                    {activeTab === 'children' ? (
                        <FlatList
                            data={filteredChildren}
                            keyExtractor={item => item.id.toString()}
                            ItemSeparatorComponent={() => <View style={{ height: 12 }} />}
                            renderItem={(child) => <Child childData={child.item} />}
                            refreshing={isRefreshing}
                            onRefresh={handleRefresh}
                        />
                    ) : (
                        <FlatList
                            data={filteredParents}
                            keyExtractor={item => item.id.toString()}
                            ItemSeparatorComponent={() => <View style={{ height: 12 }} />}
                            renderItem={(parent) => <Parent parentData={parent.item} />}
                            refreshing={isRefreshing}
                            onRefresh={handleRefresh}
                        />
                    )}
                </View>
            </View>
        </LinearGradient>

    );
}

const styles = StyleSheet.create({
    mainContainer: {
        flex: 1,
        width: '100%',
        alignItems: 'center',
        paddingTop: 30,
        paddingBottom: 10,
        paddingHorizontal: 10,
    },
    titleContainer: {
        flexDirection: 'row',
        justifyContent:'center',
        alignItems:'center',
        paddingTop:20
    },
    title:{
        fontSize:20,
        fontWeight:'500'
    },
    text: {
        fontSize: 16,
        fontWeight:'400',
    },
    lighterText:{
        fontSize:16,
        fontWeight:'200',
    },
    listContainer:{
        width:'100%',
        flex: 1,
    },
    searchContainer:{
        width:'80%',
        flexDirection:'row',
        alignItems:'center',
        justifyContent:'center',
        gap:20
        // Possible move the icon to the searchbar.
    },
    itemContainer: {
        flexDirection: 'row',
        backgroundColor:'white',
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
    inputContainer: {
        margin:5,
        width:'100%',
        paddingHorizontal: 20,
        paddingVertical: 10,
        color:'black',
        fontWeight:'400',
        fontSize:16,
        borderWidth:0,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent:'space-between',
        backgroundColor: '#CBD9FC',
        borderRadius: 50,
        gap: 10,
        outlineColor: '#000',
        outlineWidth: 0,
        shadowColor: 'gray',
        shadowRadius: 10,
        shadowOpacity: 0.15,
        shadowOffset: { width: 0, height: 4 }
    },
    tabContainer: {
        flexDirection: 'row',
        width:'100%',
        marginHorizontal: 20,
        marginTop: 20,
        marginBottom: 15,
        borderRadius: 10,
        overflow: 'hidden',
        shadowColor: 'gray',
        shadowRadius: 10,
        shadowOpacity: 0.15,
        shadowOffset: { width: 0, height: 4 }
    },
    tabButton: {
        flex: 1,
        paddingVertical: 12,
        alignItems: 'center',
        backgroundColor: 'white',
    },
    activeTabButton: {
        backgroundColor: '#9BB7F7',
    },

})
