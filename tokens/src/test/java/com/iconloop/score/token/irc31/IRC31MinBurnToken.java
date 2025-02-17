/*
 * Copyright 2021 ICONation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iconloop.score.token.irc31;

import score.annotation.External;

import java.math.BigInteger;

public class IRC31MinBurnToken extends IRC31MintBurn {

    @External(readonly=true)
    public String name() {
        return "SampleMultiToken";
    }

    @External
    public void mint(BigInteger _id, BigInteger _supply, String _uri) {
        super._mint(_id, _supply, _uri);
    }

    @External
    public void burn(BigInteger _id, BigInteger _amount) {
        super._burn(_id, _amount);
    }

    @External
    public void setTokenURI(BigInteger _id, String _uri) {
        super._setTokenURI(_id, _uri);
    }
}
