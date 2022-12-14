import React from "react";
import Sidebar from "./Sidebar";
import MapboxMap from "../map/MapboxMap";
import { listar_locais } from "../API";
import { useEffect, useState } from "react/cjs/react.development";

export default function Landing(){

    return(
        <>
            <div>
                <MapboxMap/>
            </div>
        </>
    )
}