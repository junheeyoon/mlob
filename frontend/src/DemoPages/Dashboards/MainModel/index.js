import React, {Component, Fragment} from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';

import {
    Row, Col,
    Card,
} from 'reactstrap';

import PageTitle from '../../../Layout/AppMain/PageTitle';
import avatar1 from '../../../assets/utils/images/avatars/1.jpg';
import avatar2 from '../../../assets/utils/images/avatars/2.jpg';
import avatar3 from '../../../assets/utils/images/avatars/3.jpg';
import avatar4 from '../../../assets/utils/images/avatars/4.jpg';

import {
    faCheckCircle,
    faMinusCircle,
    faTimesCircle,
    faPlusCircle
} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

export default class AnalyticsDashboard1 extends Component {
    constructor() {
        super();

        this.state = {
            dropdownOpen: false,
            activeTab1: '11',

        };
        this.toggle = this.toggle.bind(this);
        this.toggle1 = this.toggle1.bind(this);

    }

    toggle() {
        this.setState(prevState => ({
            dropdownOpen: !prevState.dropdownOpen
        }));
    }

    toggle1(tab) {
        if (this.state.activeTab1 !== tab) {
            this.setState({
                activeTab1: tab
            });
        }
    }

    render() {

        return (
            <Fragment>
                <ReactCSSTransitionGroup
                    component="div"
                    transitionName="TabsAnimation"
                    transitionAppear={true}
                    transitionAppearTimeout={0}
                    transitionEnter={false}
                    transitionLeave={false}>
                    <div>
                        <PageTitle
                            heading="Main Model"
                            subheading="Main Business Model Metrics Information Monitoring Page."
                            icon="pe-7s-albums icon-gradient bg-mean-fruit"
                        />
    
                        <Row>
                            <Col md="12">
                                <Card className="main-card mb-3">
                                    <div className="card-header">Main Model List
                                    </div>
                                    <div className="table-responsive">
                                        <table className="align-middle mb-0 table table-borderless table-striped table-hover">
                                            <thead>
                                            <tr>
                                                <th className="text-center">ID</th>
                                                <th>Main Business Model Name</th>
                                                <th className="text-center">Service</th>
                                                <th className="text-center">Drift</th>
                                                <th className="text-center">Metric</th>
                                                <th className="text-center">Avg Prediction</th>
                                                <th className="text-center">Last Prediction</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td className="text-center text-muted">#345</td>
                                                <td>
                                                    <div className="widget-content p-0">
                                                        <div className="widget-content-wrapper">
                                                            <div className="widget-content-left mr-3">
                                                                <div className="widget-content-left">
                                                                    <img width={40} className="rounded-circle" src={avatar4} alt="Avatar" />
                                                                </div>
                                                            </div>
                                                            <div className="widget-content-left flex2">
                                                                <div className="widget-heading">John Doe</div>
                                                                <div className="widget-subheading opacity-7">Web Developer</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faCheckCircle} size="2x" color="#3e783b"/>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faCheckCircle} size="2x" color="#3e783b"/>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faCheckCircle} size="2x" color="#3e783b"/>
                                                </td>
                                                <td className="text-center">24K</td>
                                                <td className="text-center">2 Hours Ago</td>
                                                
                                                
                                            </tr>
                                            <tr>
                                                <td className="text-center text-muted">#347</td>
                                                <td>
                                                    <div className="widget-content p-0">
                                                        <div className="widget-content-wrapper">
                                                            <div className="widget-content-left mr-3">
                                                                <div className="widget-content-left">
                                                                    <img width={40} className="rounded-circle" src={avatar3} alt="Avatar" />
                                                                </div>
                                                            </div>
                                                            <div className="widget-content-left flex2">
                                                                <div className="widget-heading">Ruben Tillman</div>
                                                                <div className="widget-subheading opacity-7">Etiam sit amet orci eget</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faCheckCircle} size="2x" color="#3e783b"/>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faTimesCircle} size="2x" color="#d11a2a"/>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faCheckCircle} size="2x" color="#3e783b"/>
                                                </td>
                                                <td className="text-center">24K</td>
                                                <td className="text-center">2 Hours Ago</td>
                                            </tr>
                                            <tr>
                                                <td className="text-center text-muted">#321</td>
                                                <td>
                                                    <div className="widget-content p-0">
                                                        <div className="widget-content-wrapper">
                                                            <div className="widget-content-left mr-3">
                                                                <div className="widget-content-left">
                                                                    <img width={40} className="rounded-circle" src={avatar2} alt="Avatar" />
                                                                </div>
                                                            </div>
                                                            <div className="widget-content-left flex2">
                                                                <div className="widget-heading">Elliot Huber</div>
                                                                <div className="widget-subheading opacity-7">Lorem ipsum dolor sic</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faCheckCircle} size="2x" color="#3e783b"/>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faCheckCircle} size="2x" color="#3e783b"/>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faCheckCircle} size="2x" color="#3e783b"/>
                                                </td>
                                                <td className="text-center">24K</td>
                                                <td className="text-center">2 Hours Ago</td>
                                            </tr>
                                            <tr>
                                                <td className="text-center text-muted">#55</td>
                                                <td>
                                                    <div className="widget-content p-0">
                                                        <div className="widget-content-wrapper">
                                                            <div className="widget-content-left mr-3">
                                                                <div className="widget-content-left">
                                                                    <img width={40} className="rounded-circle" src={avatar1} alt="Avatar" /></div>
                                                            </div>
                                                            <div className="widget-content-left flex2">
                                                                <div className="widget-heading">Vinnie Wagstaff</div>
                                                                <div className="widget-subheading opacity-7">UI Designer</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faCheckCircle} size="2x" color="#3e783b"/>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faCheckCircle} size="2x" color="#3e783b"/>
                                                </td>
                                                <td className="text-center">
                                                    <FontAwesomeIcon icon={faMinusCircle} size="2x" color="#708090"/>
                                                </td>
                                                <td className="text-center">24K</td>
                                                <td className="text-center">2 Hours Ago</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </Card>
                            </Col>
                        </Row>
                    </div>
                </ReactCSSTransitionGroup>
            </Fragment>
        )
    }
}
