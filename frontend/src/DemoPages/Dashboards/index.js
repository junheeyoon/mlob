import React, {Fragment} from 'react';
import {Route} from 'react-router-dom';

// DASHBOARDS

import BasicDashboard from './Basic/';
import MainModelDashboard from './MainModel/'
import SubModelDashboard from './SubModel/'
// Layout
import './test.css';    
import AppHeader from '../../Layout/AppHeader/';
import AppSidebar from '../../Layout/AppSidebar/';
import AppFooter from '../../Layout/AppFooter/';

const Dashboards = ({match}) => (
    <Fragment>
        <AppHeader/>
        <div className="app-main">
            <AppSidebar/>
            <div className="app-main__outer">
                <div className="app-main__inner">
                    <Route path={`${match.url}/basic`} component={BasicDashboard}/>

                    <Route path={`${match.url}/mainmodel`} component={MainModelDashboard}/>

                    <Route path={`${match.url}/submodel`} component={SubModelDashboard}/>
                </div>
            </div>
        </div>
    </Fragment>
);

export default Dashboards;