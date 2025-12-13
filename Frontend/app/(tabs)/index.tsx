import {FlatList, Platform, Pressable, StyleSheet, Text, TextInput, View} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { Link, Stack } from 'expo-router';
import FontAwesome6 from '@expo/vector-icons/FontAwesome6';
import FontAwesome from "@expo/vector-icons/FontAwesome";
import AntDesign from "@expo/vector-icons/AntDesign";
import {useState} from "react";

const dummyDataChildren = [
    {
        id:"1",
        name: "Bob Knupp",
    },
    {
        id:"2",
        name: "Henrik Henriksen",
    },
    {
        id:"3",
        name: "Lillian Arendal",
    },
    {
        id: "4",
        name: "Tan Syvertsen",
    },
    {
        id:"5",
        name: "Rina Jensen",
    },
    {
        id:"6",
        name: "Jonas Bjølsen",
    }
]

type ChildProps = {name: string}

const Child = ({name}: ChildProps) => {

    const [status, setStatus] = useState("Levert");

    return (
        <View style={styles.listContainer}>
            <View style={styles.listRow}>
                <View>
                <Text style={styles.listText}>{name}</Text>
                <Text style={styles.listText}>Hentet av: Bob</Text>
                </View>
                <Pressable
                    style={styles.statusButton}>
                    <Text style={styles.statusText}>Hentet</Text>
                </Pressable>
            </View>
        </View>
    );
}

export default function HomePage() {
  return (
      <LinearGradient
          colors={['#b2c9ff', '#ffffff']} // gradient colors
          style={styles.mainContainer}
          start={{ x: 0.5, y: 0 }} // gradient start point
          end={{ x: 0.5, y: 1 }}   // gradient end point
      >
          <View style={styles.titleContainer}>
              <Text style={styles.title}>Krysselista</Text>
          </View>

          <View>
              <View style={styles.searchInputView}>
                  <TextInput
                      style={[styles.textInput, {fontFamily: "Inter", fontSize: 18}]}
                      placeholder='Søk i Sommerfugl '
                  />
                  <FontAwesome6 name="magnifying-glass" size={24} color="black" />
              </View>
          </View>

          <View>
              <FlatList
                  data={dummyDataChildren}
                  renderItem={({ item }) => <Child name={item.name}/>}
                  keyExtractor={item => item.id}
                  ItemSeparatorComponent={() => <View style={{ height: 14 }}></View>}
              />
          </View>
      </LinearGradient>
  );

}

const styles = StyleSheet.create({
    mainContainer: {
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
        width: "100%",
        paddingHorizontal: 20,
        paddingTop: 48,
    },
  titleContainer: {
      flexDirection: 'row',
      gap: 8,
      justifyContent:'center',
      alignItems:'center',
      padding:18,
      paddingTop:80
  },
    title:{
        fontSize:20,
        fontWeight:'600'
    },
    textInput: {
        fontSize: 16,
        borderRadius: 40,
        color: '#00004f',
    },
    searchInputView: {
        flexDirection: 'row',
        backgroundColor: "#ffffff33",
    },
    listContainer:{
        flexDirection:'row',
        backgroundColor:'#ffffff',
        height:50,
        padding:5,
        borderRadius:10,
    },
    listRow: {
        flexDirection:'row',
        justifyContent:'space-between',
        width: '75%',

    },
    listText: {
        fontSize: 14,
    },
    statusButton: {
        borderWidth: 1,
        borderColor: '#000000',
        borderRadius: 40,
        paddingHorizontal: 30,
        paddingVertical: 10,
    },
    statusText: {
        fontSize: 18,
    },
    hentetButton: {
        borderWidth: 1,
        borderColor: '#000000',
        borderRadius: 40,
    },
    levertButton: {
        borderWidth: 1,
        backgroundColor: '#A7BFF8',
        borderRadius: 40,
    },
    fravaerButton: {
        borderWidth: 1,
        backgroundColor: '#00004F',
        color: '#ffffff',
        borderRadius: 40,
    },
    sykButton: {
        borderWidth: 1,
        backgroundColor: '#FF8BA080',
        borderRadius: 40,
    },
});
