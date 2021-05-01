#pragma once

#include <map> /* For std::map */
#include <string> /* For std::string */
#include <vector> /* For std::vector */
#include <memory> /* For smart pointer */

namespace tinyjson {

    enum class JsonValueType : int {
        NUL, // null
        NUMBER, // int, double, ...
        BOOL, // bool
        STRING, // std::string
        ARRAY, // std::vector
        OBJECT
    };

    /* null for class */
    class NullType {
    public:
        bool operator==(const NullType&) const noexcept { return true; }
        bool operator<(const NullType&) const noexcept { return false; }
    };

    class Json;

    using Array = std::vector<Json>;
    using Object = std::map<std::string, Json>;

    Json nullJson();

    std::string serialize(const NullType&);
    std::string serialize(const int& value);
    std::string serialize(const double& value);
    std::string serialize(const bool& value) ;
    std::string serialize(const std::string& value);
    std::string serialize(const Array& value);
    std::string serialize(const Object& value);
    
    class JsonValue {
    public:
        virtual ~JsonValue() {}
        virtual std::string serialize() = 0;

        virtual int toInt() const { return 0; }
        virtual double toDouble() const { return 0.0; }
        virtual bool toBool() const { return false; }
        virtual std::string toStdString() const { return std::string(); }
        virtual Array toArray() const { return Array(); }
        virtual Object toObject() const { return Object(); }

        virtual JsonValueType type() const = 0;
        virtual bool equals(const JsonValue&) const = 0;
        virtual bool operator==(const JsonValue&) const = 0;
        virtual bool operator!=(const JsonValue& rhs) const { return !((*this) == rhs); };
        virtual Json operator[](const size_t& index) const;
        virtual Json operator[](const std::string& key) const;
    };

    template<JsonValueType valueType, typename T>
    class JsonValueWrapper : public JsonValue {
    protected:
        T value;

    public:
        JsonValueWrapper() : value(T()) {}
        JsonValueWrapper(const T& value) : value(value) {}
        virtual ~JsonValueWrapper() {}

        std::string serialize() {
            return tinyjson::serialize(value);
        }

        JsonValueType type() const {
            return valueType;
        }

        bool equals(const JsonValue& other) const {
            const JsonValueWrapper<valueType, T>& rhs = static_cast<const JsonValueWrapper<valueType, T>&>(other);
            return type() == rhs.type() && value == rhs.value;
        }

        bool operator==(const JsonValue& rhs) const {
            return this->equals(rhs);
        }

    };

    class JsonNull final : public JsonValueWrapper<JsonValueType::NUL, NullType> {
    public:
        JsonNull() {}
        JsonNull(std::nullptr_t) {}
        ~JsonNull() {}

    };

    class JsonInt final : public JsonValueWrapper<JsonValueType::NUMBER, int> {
    public:
        JsonInt() {}
        JsonInt(const int& value) : JsonValueWrapper(value) {}

        int toInt() const { return value; }
        double toDouble() const { return toInt(); }

    };

    class JsonDouble final : public JsonValueWrapper<JsonValueType::NUMBER, double> {
    public:
        JsonDouble() {}
        JsonDouble(const double& value) : JsonValueWrapper(value) {}

        int toInt() const { return toDouble(); }
        double toDouble() const { return value; }

    };

    class JsonBool final : public JsonValueWrapper<JsonValueType::BOOL, bool> {
    public:
        JsonBool() {}
        JsonBool(const bool& value) : JsonValueWrapper(value) {}

        bool toBool() const { return false; }

    };

    class JsonString final : public JsonValueWrapper<JsonValueType::STRING, std::string> {
    public:
        JsonString() {}
        JsonString(const std::string& value) : JsonValueWrapper(value) {}

        std::string toStdString() { return value; }

    };

    class JsonArray final : public JsonValueWrapper<JsonValueType::ARRAY, Array> {
    public:
        JsonArray() {}
        JsonArray(const Array& value) : JsonValueWrapper(value) {}

        Array toArray() { return value; }
        Json operator[](const size_t& index) const;

    };

    class JsonObject final : public JsonValueWrapper<JsonValueType::OBJECT, Object> {
    public:
        JsonObject() {}
        JsonObject(const Object& value) : JsonValueWrapper(value) {}

        Object toObject() { return value; }
        Json operator[](const std::string& key) const;

    };

    class Json final {
    private:
        std::shared_ptr<JsonValue> pJsonValue;

    public:
        /* Constructors */
        /* null */
        Json() : pJsonValue(std::make_shared<JsonNull>()) {}
        Json(std::nullptr_t) : pJsonValue(std::make_shared<JsonNull>()) {}
        /* number */
        Json(const int& value) : pJsonValue(std::make_shared<JsonInt>(value)) {}
        Json(const double& value) : pJsonValue(std::make_shared<JsonDouble>(value)) {}
        /* bool */
        Json(const bool& value) : pJsonValue(std::make_shared<JsonBool>(value)) {}
        /* std::string */
        Json(const std::string& value) : pJsonValue(std::make_shared<JsonString>(value)) {}
        /* Array */
        Json(const Array& value) : pJsonValue(std::make_shared<JsonArray>(value)) {}
        /* Object */
        Json(const Object& value) : pJsonValue(std::make_shared<JsonObject>(value)) {}
        ~Json() {}

        template <class M, typename std::enable_if<std::is_constructible<std::string, decltype(std::declval<M>().begin()->first)>::value &&
        std::is_constructible<Json, decltype(std::declval<M>().begin()->second)>::value, int>::type = 0>
        Json(const M & m) : Json(Object(m.begin(), m.end())) {}

        template <class V, typename std::enable_if<std::is_constructible<Json, decltype(*std::declval<V>().begin())>::value, int>::type = 0>
        Json(const V & v) : Json(Array(v.begin(), v.end())) {}

        template <class T, class = decltype(&T::toJson)>
        Json(const T& t) : Json(t.toJson()) {}

        std::string serialize() { return pJsonValue->serialize(); }

        int toInt() { return pJsonValue->toInt(); }
        double toDouble() { return pJsonValue->toDouble(); }
        bool toBool() const { return pJsonValue->toBool(); }
        std::string toStdString() const { return pJsonValue->toStdString(); }
        Array toArray() const { return pJsonValue->toArray(); }
        Object toObject() const { return pJsonValue->toObject(); }

        JsonValueType type() const { return pJsonValue->type(); }

        bool equals(const Json& other) const { return pJsonValue->equals((*other.pJsonValue.get())); }
        bool operator==(const Json& other) const { return equals(other); }
        bool operator!=(const Json& other) const { return !((*this) == other); }
        Json operator[](const size_t& index) const { return (*pJsonValue)[index]; };
        Json operator[](const std::string& key) const { return (*pJsonValue)[key]; }
    };

    Json nullJson() {
        static Json json;
        return json;
    }

    std::string serialize(const NullType&) {
        return "null";
    }

    std::string serialize(const int& value) {
        char buf[32];
        snprintf(buf, sizeof(buf), "%d", value);
        return std::string(buf);
    }

    std::string serialize(const double& value) {
        char buf[32];
        snprintf(buf, sizeof(buf), "%.17g", value);
        return std::string(buf);
    }

    std::string serialize(const bool& value) {
        return value ? "true" : "false";
    }

    std::string serialize(const std::string& value) {
        std::string output;
        output += '"';
        for (int i = 0, len = value.length(); i < len; i++) {
            char ch = value[i];
            if (ch == '\\') {
                output += "\\\\";
            } 
            else if (ch == '"') {
                output += "\\\"";
            } 
            else if (ch == '\b') {
                output += "\\b";
            } 
            else if (ch == '\f') {
                output += "\\f";
            } 
            else if (ch == '\n') {
                output += "\\n";
            } 
            else if (ch == '\r') {
                output += "\\r";
            } 
            else if (ch == '\t') {
                output += "\\t";
            } 
            else if (static_cast<uint8_t>(ch) <= 0x1f) {
                char buf[8];
                snprintf(buf, sizeof buf, "\\u%04x", ch);
                output += buf;
            } else if (static_cast<uint8_t>(ch) == 0xe2 && 
                    static_cast<uint8_t>(value[i+1]) == 0x80 &&
                    static_cast<uint8_t>(value[i+2]) == 0xa8) {
                output += "\\u2028";
                i += 2;
            } else if (static_cast<uint8_t>(ch) == 0xe2 && 
                    static_cast<uint8_t>(value[i+1]) == 0x80 && 
                    static_cast<uint8_t>(value[i+2]) == 0xa9) {
                output += "\\u2029";
                i += 2;
            } else {
                output += ch;
            }
        }
        output += '"';
        return output;
    }

    std::string serialize(const Object& value) {
        bool flag = true;
        std::string output;
        output += "{";
        for (auto& kv : value) {
            if (!flag) {
                output += ", ";
            }
            output += serialize(kv.first);
            output += ": ";
            output += const_cast<Json&>(kv.second).serialize();
            flag = false;
        }
        output += "}";
        return output;
    }

    std::string serialize(const Array& value) {
        bool flag = true;
        std::string output;
        output += "[";
        for (auto& val : value) {
            if (!flag) {
                output += ", ";
            }
            output += const_cast<Json&>(val).serialize();
            flag = false;
        }
        output += "]";
        return output;
    }

    Json JsonValue::operator[](const size_t& index) const { return nullJson(); }

    Json JsonValue::operator[](const std::string& key) const { return nullJson(); }

    Json JsonArray::operator[](const size_t& index) const {
        if (index < value.size()) {
            return value[index];
        }
        return nullJson();
    }

    Json JsonObject::operator[](const std::string& key) const {
        const auto& iter = value.find(key);
        if (value.end() != iter) {
            return iter->second;
        }
        return nullJson();
    }

}