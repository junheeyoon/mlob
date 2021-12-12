import React, {Fragment} from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {
    Col, Row, Card, CardBody,
    CardTitle, Button, Form, FormGroup, Label, Input
} from 'reactstrap';
import { Link } from "react-router-dom";

export default class FormGridFormRow extends React.Component {
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
                    <Card className="main-card mb-3">
                        <CardBody>
                            <CardTitle>Sign In</CardTitle>
                            <Form>
                                <FormGroup>
                                    <Input type="text" name="UserName" id="exampleUserName"
                                          placeholder="UserName"/>
                                </FormGroup>
                                <FormGroup>
                                    <Input type="text" name="Password" id="examplePassword"
                                           placeholder="Password"/>
                                </FormGroup>
          
                                <FormGroup check>
                                    <Input type="checkbox" name="check" id="exampleCheck"/>
                                    <Label for="exampleCheck" check>Check me out</Label>
                                </FormGroup>
                                <Link to= '/dashboards/basic'><Button color="primary" className="mt-2 btn-block">Sign in</Button></Link>
                            </Form>

                        </CardBody>
                    </Card>
                </ReactCSSTransitionGroup>
            </Fragment>
        );
    }
}
