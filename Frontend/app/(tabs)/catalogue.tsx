import { Image } from 'expo-image';
import {Platform, StyleSheet, View, Text, Animated, FlatList, TextInput, TouchableOpacity, Modal} from 'react-native';
import FontAwesome from '@expo/vector-icons/FontAwesome';
import { LinearGradient } from 'expo-linear-gradient';
import AntDesign from '@expo/vector-icons/AntDesign';


import { Collapsible } from '@/components/ui/collapsible';
import { ExternalLink } from '@/components/external-link';
import ParallaxScrollView from '@/components/parallax-scroll-view';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { IconSymbol } from '@/components/ui/icon-symbol';
import { Fonts } from '@/constants/theme';
import ScrollView = Animated.ScrollView;
import React, {useRef, useState} from "react";
import Ionicons from "@expo/vector-icons/Ionicons";

const dummyData = [
    {
        id:"1234",
        name: "Emilie",
        phoneNumber: 4746126406
    },
    {    id:"1223334",
        name: "AK og sånt",
        phoneNumber: 4746126406
    },
    {
        id:"1223423434",
        name: "Eirin",
        phoneNumber: 4746126406
    }
]
    // I will do this with "avdeling" dummy data, but need to discuss other sorting options.
const DropDownMenu = () => {
    const [menuOpen, setMenuOpen] = useState(false);
    const [selected, setSelected] = useState("Avdeling");
    const [buttonLayout, setButtonLayout] = useState({ x: 0, y: 0, width: 0, height: 0 });
    const buttonRef = useRef< typeof TouchableOpacity>(null);

    const options = ["Sommerfugl","Marihone","Humle"];

    function handleSelect(value: any){
        setSelected(value);
        setMenuOpen(false);
    }
    const getButtonPosition = () => {
        if (buttonRef.current) {
            // @ts-ignore
            buttonRef.current.measure((fx, fy, width, height, px, py) => {
                setButtonLayout({
                    x: px,
                    y: py,
                    width,
                    height
                });
            });
        }
    };

    const styles = StyleSheet.create({
        dropDownContainer: {
            position: 'absolute',
            backgroundColor: 'white',
            borderRadius: 10,
            overflow: 'hidden',
            width: buttonLayout.width,
            shadowColor: 'gray',
            shadowRadius: 10,
            shadowOpacity: 0.15,
            shadowOffset: { width: 0, height: 4 }
        },
        dropDownItem: {
            padding: 16,
            width: '100%',
            alignItems: 'center',
            justifyContent: 'center',
        },
        dropDownItemActive: {
            backgroundColor: '#cad8ff'
        },
        buttonContainer: {
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'center',
            width: '100%',
            minWidth:120, // How to make it responsive?
            maxWidth:200,
            backgroundColor: '#dbe3ff',
            paddingHorizontal: 20,
            paddingVertical: 10,
            borderRadius: 50,
            shadowColor: 'gray',
            shadowRadius: 10,
            shadowOpacity: 0.15,
            shadowOffset: { width: 0, height: 4 }
        },
        dropDownText:{
            fontSize: 16,
            fontWeight: '300',
            color:'gray'
        },
        modalOverlay: {
            flex: 1,
            backgroundColor: 'transparent',
        },
        modalContent: {
            flex: 1,
            position: 'relative',
        },
        wrapper: {
            position: 'relative',
        },

    });

    // @ts-ignore
    return (
        <View style={styles.wrapper}>
            <TouchableOpacity
                ref={buttonRef}
                style={styles.buttonContainer}
                onPress={() => {
                    getButtonPosition();
                    setMenuOpen(true);
                }}
                onLayout={getButtonPosition}
            >
                <Text style={styles.dropDownText}>{selected}</Text>

            </TouchableOpacity>

            <Modal
                visible={menuOpen}
                transparent={true}
                animationType="fade"
                onRequestClose={() => setMenuOpen(false)}
            >
                <TouchableOpacity
                    style={styles.modalOverlay}
                    activeOpacity={1}
                    onPress={() => setMenuOpen(false)}
                >
                    <View style={styles.modalContent}>
                        <View
                            style={[
                                styles.dropDownContainer,
                                {
                                    top: buttonLayout.y + buttonLayout.height + 5,
                                    left: buttonLayout.x,
                                }
                            ]}
                        >
                            {options.map((option) => (
                                <TouchableOpacity
                                    style={[
                                        styles.dropDownItem,
                                        option === selected && styles.dropDownItemActive
                                    ]}
                                    key={option}
                                    onPress={() => handleSelect(option)}
                                >
                                    <Text style={styles.dropDownText}>{option}</Text>
                                </TouchableOpacity>
                            ))}
                        </View>
                    </View>
                </TouchableOpacity>
            </Modal>
        </View>
    );
}
type ParentProps = {name:string, phoneNumber: number}

const Parent = ({name,phoneNumber}:ParentProps) =>{
    return (
        <View style={styles.itemContainer}>
            <View style={{display:'flex', flexDirection:'row',alignItems:'center',gap:10}}>
                <Text style={{fontSize:30}}>🧑‍🦱</Text>
                <View style={{gap:8}}>
                    <Text style={styles.text}>{name}</Text>
                    <Text style={styles.lighterText}>
                        <FontAwesome name="phone" size={14} color="gray" /> {phoneNumber}
                    </Text>
                </View>
            </View>
            <View>
                <AntDesign name="caret-right" size={14} color="gray" />
            </View>
        </View>
        );
  }


export default function CataloguePage() {

    const [text, onChangeText] = React.useState("");
    return (
        <LinearGradient
            colors={['#b2c9ff', '#ffffff']} // gradient colors
            style={styles.mainContainer}
            start={{ x: 0.5, y: 0 }} // gradient start point
            end={{ x: 0.5, y: 1 }}   // gradient end point
        >

            <View style={styles.titleContainer}>
                <Text style={styles.title}>Katalog</Text>
            </View>
            <View style={styles.searchContainer}>
                <TextInput
                    placeholder={"Søk"}
                    style={styles.inputContainer}
                    onChangeText={onChangeText}
                    value={text}
                />

                <DropDownMenu/>
            </View>

            <View style={styles.listContainer}>
                <FlatList
                    data={dummyData}
                    renderItem={({ item }) => <Parent name={item.name} phoneNumber={item.phoneNumber} />}
                    keyExtractor={item => item.id}
                    ItemSeparatorComponent={() => <View style={{ height: 24 }}></View>}
                />
            </View>


        </LinearGradient>

    );
}

const styles = StyleSheet.create({
    mainContainer:{
        flex: 1,
        alignItems:'center',
        paddingTop:10,
        paddingBottom:10,
        paddingHorizontal:20,
    },
    titleContainer: {
        flexDirection: 'row',
        justifyContent:'center',
        alignItems:'center',
        padding:20,
    },
    title:{
        fontSize:20,
        fontWeight:'400'
    },
    text: {
        fontSize: 16,
        fontWeight:'300',
    },
    lighterText:{
        fontSize:16,
        fontWeight:'200',
    },
    listContainer:{
        width:'100%',
    },
    searchContainer:{
       width:'100%',
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
        margin:10,
        width:'60%',
        paddingHorizontal: 20,
        paddingVertical: 10,
        color:'black',
        fontWeight:'200',
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


})
