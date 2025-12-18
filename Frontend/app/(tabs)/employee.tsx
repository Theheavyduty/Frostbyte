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
import FontAwesome from "@expo/vector-icons/FontAwesome";


export default function EmployeePage() {
    return (
        <LinearGradient
            colors={['#b2c9ff', '#ffffff']}
            locations={[0, 0.12]}
            start={{ x: 0.5, y: 0 }}
            end={{ x: 0.5, y: 1 }}
            style={styles.mainContainer}
        ><View style={styles.titleContainer}>
            <Text style={styles.title}>Min profil</Text>
        </View>
            <ScrollView
                style={styles.scrollView}
                contentContainerStyle={styles.contentContainer}
                showsVerticalScrollIndicator={false}
            >
                <Text style={{fontSize:100}}>🧑‍🦱</Text>
                <Text style={styles.biggerText}>
                    Andreas Karlsberg
                </Text>
                <View style={styles.infoContainer}>
                    <Text style={styles.mediumText}>Kontaktinfo:</Text>
                    <Text style={styles.lighterText}>
                        Telefonnummer: +47461326405
                    </Text>
                    <Text style={styles.lighterText} >
                        Epost: andreaskar@gmail.com
                    </Text>
                    <Text style={styles.lighterText}>
                        Addresse: Mostubben 94,1555,Myrvoll
                    </Text>
                    <Text style={styles.lighterText}>
                        Avdeling: Sommerfugl
                    </Text>
                </View>
                <Pressable style={styles.button}>
                    <Text style={styles.text}>Endre innloginskode</Text>
                </Pressable>
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
        paddingTop:75   ,
        padding:20
    },
    title:{
        fontSize:20,
        fontWeight:'500',
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
        fontWeight:'200',
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
});