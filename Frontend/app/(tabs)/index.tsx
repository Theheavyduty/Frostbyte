import {
    Alert,
    FlatList,
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    View
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import EvilIcons from '@expo/vector-icons/EvilIcons';
import React, {useEffect, useState} from "react";
import {ChildData, ChildStatus, ChildWithStatus, FilterStatus, Status} from "@/types/child";
import { getAllChildrenData }from "@/api/childApi";
import {EmployeeData} from "@/types/employee";
import SykModal from "@/components/SykModal";
import FravaerModal from "@/components/FravaerModal";
import {router, useRouter} from "expo-router";
import DropDownMenuCrosslist from "@/components/DropDownMenuCrosslist";
import { getStatus, registerStatusApi, registerSyk, registerFravaer, getChildrenBySearch} from "@/api/statusApi";
import { statusToLabel, labelToStatus, filterStatusToLabel, labelToFilterStatus} from "@/utils/statusMappings";
import { statusStyles } from "@/components/ui/statusStyles";

export default function HomePage() {
    const [children, setChildren] = useState<ChildWithStatus[]>([]);
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [filterStatus, setFilterStatus] = useState<FilterStatus>("ALLE");
    const [showSykModal, setShowSykModal] = useState(false);
    const [showFravaerModal, setShowFravaerModal] = useState(false);
    const [selectedChildId, setSelectedChildId] = useState<number | null>(null);
    const [searchText, setSearchText] = useState("");

    const router = useRouter();

    function updateChildStatus(childId: number, newStatus: Status) {
        setChildren(prev =>
            prev.map(child =>
                child.id === childId ? { ...child, status: newStatus } : child
            )
        );
    }

    async function fetchChildStatus(child: ChildData): Promise<ChildWithStatus> {
        try {
            const latestStatus = await getStatus(child.id);
            if (!latestStatus) {
                return {
                    ...child, status: "HENTET",
                    updatedAt: undefined };
            } return {
                ...child,
                status: latestStatus.status,
                updatedAt: latestStatus.eventTime };
        } catch (error) {
            console.error(`Feil ved henting av status for child ${child.id}:`, error);
            return { ...child, status: "HENTET", updatedAt: undefined };
        }
    }
    async function getLatestStatus(childrenData: ChildData[]): Promise<ChildWithStatus[]> {
        return Promise.all(
            childrenData.map(fetchChildStatus)
        );
    }

    async function fetchChildren() {
        setIsRefreshing(true);
        try {
            const childrenData = await getAllChildrenData();
            const childStatusList = await getLatestStatus(childrenData);
            setChildren((childStatusList));
        } catch(error) {
            console.error("Error in fetching children" + error);
        } finally {
            setIsRefreshing(false);
        }
    }

    async function registerStatus(
        childId: number,
        employeeId: number,
        status: Status,
        suppliedData?: {symptoms?: string; reason?: string}
    ) {
        try {
            switch (status) {
                case "SYK":
                    return await registerSyk(childId, employeeId, suppliedData?.symptoms ?? "");
                case "FRAVAER":
                    return await registerFravaer(childId, employeeId, suppliedData?.reason ?? "");
                default:
                    return await registerStatusApi(childId, status, employeeId);
            }
        } catch (error) {
            console.error("Could not register new status" + error);
        }
    }

    async function searchChildNames() {
        setIsRefreshing(true);
        try {
            const childrenData = await getChildrenBySearch(searchText);
            const childStatusList = await getLatestStatus(childrenData);
            setChildren(childStatusList);
        } catch (error) {
            console.log(error);
            Alert.alert("Kunne ikke hente søk: " + error);
        }
        setIsRefreshing(false);
    }

    useEffect(() => {
        const delayBounce = setTimeout(() => {
            searchChildNames();
        }, 800);
        return () => {
            clearTimeout(delayBounce);
        }
    }, [searchText]);

    useEffect(() => {
        fetchChildren();
    },[]);

const loggedInUser: EmployeeData = {
    id: 1,
    name: "Trollmor",
    email: "trollmor@eksempel.no",
    phoneNumber: 37379113,
    address: "Trollskogen 13",};


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

          <View style={styles.searchAndFilterContainer}>
              <View style={[styles.searchAndFilterInputView, {backgroundColor: '#ffffff33'}]}>
                  <TextInput
                      style={styles.inputContainer}
                      value={searchText}
                      onChangeText={setSearchText}
                      placeholder='Søk i navn her'
                  />
                  <EvilIcons name="search" size={32} color="#6C6868" style={{ paddingTop: 2 }} />
              </View>
              <View style={styles.searchAndFilterInputView}>
                  <DropDownMenuCrosslist
                    selected={filterStatusToLabel[filterStatus]}
                    onSelect={(newLabel) => {
                            setFilterStatus(labelToFilterStatus[newLabel]);
                        }
                    }
                    options={Object.values(filterStatusToLabel)}
                  />
              </View>
          </View>

          <View style={styles.listWrapper}>
              <FlatList
                  data={children.filter(child =>
                      filterStatus === "ALLE" || child.status === filterStatus
                  )}
                  keyExtractor={item => item.id.toString()}
                  renderItem={({ item }) => (
                      <View style={styles.listItem}>
                          <View style={styles.listRow}>
                          <TouchableOpacity
                              onPress={() => router.push(
                                `/child-details/${item.id}`
                              )}>

                              <View>
                                  <Text style={styles.listText}>{item.name}</Text>
                              </View>
                         </TouchableOpacity>
                              <DropDownMenuCrosslist
                                  selected={statusToLabel[item.status]}
                                  onSelect={(newLabel: string) => {
                                      const newStatus = labelToStatus[newLabel]
                                      if (newStatus === "SYK") {
                                          setSelectedChildId(item.id)
                                          setShowSykModal(true);
                                      } else if (newStatus === "FRAVAER") {
                                          setSelectedChildId(item.id)
                                          setShowFravaerModal(true);
                                      } else {
                                          updateChildStatus(item.id, newStatus);
                                      }
                                  }}
                                  options={Object.values(statusToLabel).filter(label => label !== "ALLE")}
                              />
                          </View>
                      </View>
                  ) }
                  ItemSeparatorComponent={() => <View style={{ height: 14 }}></View>}
              />
          <SykModal
              isVisible={showSykModal}
              setIsVisible={setShowSykModal}
              onSubmit={async (symptoms) => {
                  if(selectedChildId) {
                      await registerStatus(selectedChildId, loggedInUser.id, "SYK", {symptoms});
                      updateChildStatus(selectedChildId, "SYK");
                  }
          }}/>
          <FravaerModal
              isVisible={showFravaerModal}
              setIsVisible={setShowFravaerModal}
              onSubmit={async (reason) => {
                  if(selectedChildId) {
                    await registerStatus(selectedChildId, loggedInUser.id, "FRAVAER", {reason});
                  updateChildStatus(selectedChildId, "FRAVAER");
                  }
          }}/>
          </View>
      </LinearGradient>
  );
}

const styles = StyleSheet.create({
    mainContainer: {
        flex: 1,
        alignItems: "center",
        paddingVertical: 10,
        paddingHorizontal: 20,
    },
  searchAndFilterContainer: {
        width: "100%",
      flexDirection: 'row',
      gap: 12,
      justifyContent:'space-between',
      alignItems:'center',
  },
    titleContainer: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 20,
    },
    title:{
        marginTop:45,
        fontSize:20,
        fontWeight:'500'
    },
    filterBtn: {
        flexDirection: 'row',
        paddingHorizontal: 16,
        paddingVertical: 8,
        color: '#6C6868',
    },
    inputContainer: {
        flexDirection: 'row',
        width: 200,
        fontSize: 16,
        color: '#00004f',
        paddingHorizontal: 20,
        paddingLeft: 12,
        borderRadius: 40,
    },
    searchAndFilterInputView: {
        flexDirection: 'row',
        borderRadius: 40,
        marginBottom: 8,
        paddingRight: 8,
        outlineColor: '#000',
        outlineWidth: 0,
        shadowColor: 'gray',
        shadowRadius: 10,
        shadowOpacity: 0.15,
        shadowOffset: { width: 0, height: 4 }
    },
    listContainer:{
        flexDirection:'row',
        backgroundColor:'#ffffff',
        height:50,
        padding:5,
        borderRadius:10,
    },
    listWrapper: {
        flex: 1,
        width: '100%',
    },
    listRow: {
        flex: 1,
        flexDirection:'row',
        justifyContent:'space-between',
    },
    listItem: {
        flexDirection: 'row',
        backgroundColor: '#ffffff',
        padding: 10,
        borderRadius: 10,
        alignItems: 'center',
        justifyContent: 'space-between',
    },
    listText: {
        fontSize: 18,
        fontWeight:'400',
    },
});