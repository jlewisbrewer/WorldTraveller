'use strict';


const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {countries: []};
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/countries'}).done(response => {
            this.setState({countries: response.entity._embedded.countries});
        });
    }

    render() {
        console.log(this.state.countries);
        return (
            <CountryList countries= {
                this.state.countries}/>
        )
    }
}

class CountryList extends React.Component {
    render() {
        const countries = this.props.countries.map(country =>
            <Country key = {country._links.self.href} country = {country}/>
        );
        return (
            <table>
                <tbody>
                    <tr>
                        <th>
                            Country Name
                        </th>
                        <th>
                            Land Area
                        </th>
                    </tr>
                    {countries}
                </tbody>
            </table>
        )
    }
}

class Country extends React.Component {
    render() {
        return(
            <tr>
                <td>
                    {this.props.country.name}
                </td>
                <td>
                    {this.props.country.landArea}
                </td>
            </tr>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)